package org.mroki58.restapigithub;

import org.mroki58.restapigithub.dto.BranchDto;
import org.mroki58.restapigithub.dto.RepositoryDto;
import org.mroki58.restapigithub.dto.RepositoryWithBranchDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.util.Pair;

import static org.mroki58.restapigithub.utils.FilterUtils.filterNotForks;

@Service
public class GithubService {
    private final RestTemplate restTemplate;
    private final AsyncGithubService asyncGithubService;

    @Value("${github.url.repos}")
    private String reposUrl;

    public GithubService(RestTemplate restTemplate, AsyncGithubService asyncGithubService) {
        this.restTemplate = restTemplate;
        this.asyncGithubService = asyncGithubService;
    }

    public List<RepositoryWithBranchDto> getPublicRepositoriesWithBranches(String username) {
        List<RepositoryDto> repositories = getAllUsersRepositories(username);
        List<RepositoryDto> notForks = filterNotForks(repositories);

        List<Pair<RepositoryDto, CompletableFuture<List<BranchDto>>>> futures = new ArrayList<>();

        for (RepositoryDto repo : notForks) {
            CompletableFuture<List<BranchDto>> futureBranches = asyncGithubService.getBranchesForRepository(username, repo.getName());
            futures.add(new Pair<>(repo, futureBranches));
        }

        return futures.stream()
                .map(pair -> {
                    RepositoryDto repo = pair.getKey();
                    List<BranchDto> branches = pair.getValue().join();

                    List<RepositoryWithBranchDto.Branch> br = branches.stream()
                            .map(b -> new RepositoryWithBranchDto.Branch(b.getName(), b.getCommit().getSha()))
                            .toList();

                    return new RepositoryWithBranchDto(repo.getName(), repo.getOwner().getLogin(), br);
                })
                .toList();


    }

    public List<RepositoryDto> getAllUsersRepositories(String username)
    {
        String url =  String.format(reposUrl, username);
        RepositoryDto[] response = restTemplate.getForEntity(url, RepositoryDto[].class).getBody();
        return Arrays.asList(Optional.ofNullable(response).orElse(new RepositoryDto[0]));
    }
}
