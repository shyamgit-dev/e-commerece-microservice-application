package com.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient userClient()
    {
        return RestClient.builder()
                .baseUrl("http://localhost:8080/api/users")
                .build();
    }

    @Bean
    public RestClient productClient()
    {
        return RestClient.builder()
                .baseUrl("http://localhost:8081/api/products")
                .build();
    }
}
