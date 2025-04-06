package com.eventregistration.config;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DotenvConfig {

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
            System.out.println("Loaded: " + entry.getKey() + "=" + entry.getValue());
        });
        System.out.println("REDIS_HOST from System: " + System.getProperty("REDIS_HOST"));
        System.out.println("REDIS_PORT from System: " + System.getProperty("REDIS_PORT"));
        System.out.println("REDIS_PASSWORD from System: " + System.getProperty("REDIS_PASSWORD"));
    }

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().ignoreIfMissing().load();
    }
}
