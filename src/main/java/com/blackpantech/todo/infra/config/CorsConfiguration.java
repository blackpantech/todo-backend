package com.blackpantech.todo.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for CORS configuration
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(final CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/tasks/**")
                .allowedOrigins(allowedOrigins)
                .allowedHeaders(
                        HttpHeaders.ACCEPT,
                        HttpHeaders.CONTENT_TYPE
                );
    }

}
