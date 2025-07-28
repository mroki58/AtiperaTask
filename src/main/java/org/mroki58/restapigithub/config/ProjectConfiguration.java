package org.mroki58.restapigithub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAsync
public class ProjectConfiguration {
    @Value("${my.secrets.GITHUB_TOKEN}")
    private String githubToken;

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder()
                .interceptors((request, body, execution) -> {
                    request.getHeaders().add("Authorization", "Bearer " + githubToken);
                    return execution.execute(request, body);
                });
    }

    // Bean RestTemplate oparty o RestTemplateBuilder
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

}
