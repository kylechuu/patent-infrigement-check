package com.patlytics.take_home.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DownStreamConfig {
    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(openAiApiUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .build();
    }

}
