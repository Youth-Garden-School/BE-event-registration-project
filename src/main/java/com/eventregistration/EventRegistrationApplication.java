package com.eventregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class EventRegistrationApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
            log.info(null, "{} = {}", entry.getKey(), entry.getValue());
        });

        SpringApplication.run(EventRegistrationApplication.class, args);
    }
}
