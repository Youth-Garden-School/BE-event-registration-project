package com.eventregistration.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Component
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnvConfig {

    @NotBlank(message = "CLIENT_URL_REQUIRED")
    @Value("${client.url}")
    String clientUrl;
}
