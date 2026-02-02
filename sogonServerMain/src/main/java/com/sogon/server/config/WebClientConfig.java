package com.sogon.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        // Python AI 서버 주소 (FastAPI)
        return WebClient.builder()
                .baseUrl("http://localhost:8001") 
                .build();
    }
}
