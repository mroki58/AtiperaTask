package org.mroki58.restapigithub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mroki58.restapigithub.dto.RepositoryDto;
import org.mroki58.restapigithub.dto.RepositoryWithBranchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class GithubApiIntegrationTest {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        restTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    void shouldReturnRepositoriesWithBranches_whenUserExists() {
        // given
        String username = "octocat";
        String url = String.format("http://localhost:%d/?username=%s", port, username);

        RestTemplate githubRestTemplate = new RestTemplate();
        String githubApiUrl = String.format("https://api.github.com/users/%s/repos", username);

        ResponseEntity<List<RepositoryDto>> githubResponse = githubRestTemplate.exchange(
                githubApiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<RepositoryDto> allRepos = githubResponse.getBody();
        assertThat(allRepos).isNotNull();

        Set<String> expectedNonForkNames = allRepos.stream()
                .filter(repo -> Boolean.FALSE.equals(repo.getFork()))
                .map(RepositoryDto::getName)
                .collect(Collectors.toSet());

        // when
        ResponseEntity<RepositoryWithBranchDto[]> response = restTemplate.getForEntity(url, RepositoryWithBranchDto[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        RepositoryWithBranchDto[] body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.length).isEqualTo(expectedNonForkNames.size());

        Set<String> actualNames = Arrays.stream(body)
                .map(RepositoryWithBranchDto::getRepositoryName)
                .collect(Collectors.toSet());

        assertThat(actualNames).isEqualTo(expectedNonForkNames);

        for (RepositoryWithBranchDto repo : body) {
            assertThat(repo.getRepositoryName()).isNotNull();
            assertThat(repo.getOwnerLogin()).isEqualToIgnoringCase(username);
            assertThat(repo.getBranches()).isNotNull();

            for (RepositoryWithBranchDto.Branch branch : repo.getBranches()) {
                assertThat(branch.getName()).isNotBlank();
                assertThat(branch.getSha()).isNotBlank();
            }
        }
    }
}
