package org.mroki58.restapigithub;

import org.mroki58.restapigithub.dto.BranchDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncGithubService {
    private final RestTemplate restTemplate;

    @Value("${github.url.branches}")
    private String branchesUrl;

    public AsyncGithubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
