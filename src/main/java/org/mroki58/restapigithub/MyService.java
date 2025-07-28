package org.mroki58.restapigithub;

import org.mroki58.restapigithub.dto.BranchDto;
import org.mroki58.restapigithub.dto.RepositoryDto;
import org.mroki58.restapigithub.dto.RepositoryWithBranchDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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
public class MyService {
    private final RestTemplate restTemplate;

    @Value("${github.url.repos}")
    private String reposUrl;

    @Value("${github.url.branches}")
    private String branchesUrl;

    public MyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepositoryWithBranchDto> getPublicRepositoriesWithBranches(String username) {
        List<RepositoryDto> repositories = getAllUsersRepositories(username);
        List<RepositoryDto> notForks = filterNotForks(repositories);

        List<Pair<RepositoryDto, CompletableFuture<List<BranchDto>>>> futures = new ArrayList<>();

        for (RepositoryDto repo : notForks) {
            CompletableFuture<List<BranchDto>> futureBranches = getBranchesForRepository(username, repo.getName());
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

    @Async
    public CompletableFuture<List<BranchDto>> getBranchesForRepository(String username, String repositoryName)
    {
        String url =  String.format(branchesUrl, username, repositoryName);
        BranchDto[] response = restTemplate.getForEntity(url, BranchDto[].class).getBody();
        List<BranchDto> branches = Arrays.asList(Optional.ofNullable(response).orElse(new BranchDto[0]));
        return CompletableFuture.completedFuture(branches);
    }
}
