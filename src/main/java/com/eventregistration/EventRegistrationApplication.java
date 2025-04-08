package com.eventregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EventRegistrationApplication {

    public static void main(String[] args) {
        String activeProfile = System.getProperty("spring.profiles.active");
        
        // Only load .env file if not in production
        if (!"prod".equals(activeProfile)) {
            try {
                Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
                dotenv.entries().forEach(entry -> {
                    System.setProperty(entry.getKey(), entry.getValue());
                    log.info("{} = {}", entry.getKey(), entry.getValue());
                });
            } catch (Exception e) {
                log.warn("Could not load .env file, continuing with system environment variables");
            }
        }

        SpringApplication.run(EventRegistrationApplication.class, args);
    }
}
