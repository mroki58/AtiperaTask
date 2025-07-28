package org.mroki58.restapigithub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestApiGithubApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiGithubApplication.class, args);
    }

    @Value("${my.secrets.GITHUB_TOKEN}")
    private String githubToken;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + githubToken);
            return execution.execute(request, body);
        });

        return restTemplate;
    }


}
