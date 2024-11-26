package com.mozi.moziserver.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class GithubModelsConfig {
    @Value("${github.models.apiKey}")
    private String githubModelsApiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + githubModelsApiKey);
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}
