package org.mroki58.restapigithub;

import org.mroki58.restapigithub.dto.BranchDto;
import org.mroki58.restapigithub.dto.RepositoryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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

    public List<RepositoryDto> getAllUsersRepositories(String username)
    {
        String url =  String.format(reposUrl, username);
        System.out.println(url);
        return Arrays.asList(restTemplate.getForEntity(url, RepositoryDto[].class).getBody());
    }


    public List<BranchDto> getBranchesForRepository(String username, String repositoryName)
    {
        String url =  String.format(branchesUrl, username, repositoryName);
        System.out.println(url);
        return Arrays.asList(restTemplate.getForEntity(url, BranchDto[].class).getBody());
    }
}
