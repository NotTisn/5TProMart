package com.fivetpromart.infrastructure.analytics;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration for the Analytics service integration.
 */
@Configuration
public class AnalyticsConfig {

    /**
     * RestTemplate for calling the Python AI analytics service.
     * Configured with reasonable timeouts for analytics queries.
     */
    @Bean
    public RestTemplate analyticsRestTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(30))  // Analytics can be slow
                .build();
    }
}
