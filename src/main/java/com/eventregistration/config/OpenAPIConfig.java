package com.eventregistration.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Bean
    GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs) {
        return GroupedOpenApi.builder()
                .group(apiDocs) // /v3/api-docs/api-service
                .packagesToScan("com.eventregistration.controller")
                .build();
    }

    @Bean
    OpenAPI openAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.server.local}") String localUrl,
            @Value("${openapi.service.server.dev}") String devUrl) {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url(localUrl).description("Local Server"),
                        new Server().url(devUrl).description("Development Server")))
                .info(new Info()
                        .title(title)
                        .description("Event Registration System API Documentation - Comprehensive API for managing events, registrations, calendars, and users")
                        .version(version)
                        .contact(new Contact()
                                .name("Event Registration Team")
                                .email("support@eventregistration.com")
                                .url("https://eventregistration.com"))
                        .termsOfService("https://eventregistration.com/terms")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT Bearer token **_only_**"))
                        .addSecuritySchemes(
                                "oauth2",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .description("OAuth2 authentication")
                                        .flows(new io.swagger.v3.oas.models.security.OAuthFlows()
                                                .authorizationCode(new io.swagger.v3.oas.models.security.OAuthFlow()
                                                        .authorizationUrl("/api/v1/auths/oauth2/login/google")
                                                        .tokenUrl("/api/v1/auths/oauth2/login-success")
                                                        .scopes(new io.swagger.v3.oas.models.security.Scopes()
                                                                .addString("profile", "User profile information")
                                                                .addString("email", "User email address"))))))
                .security(List.of(
                        new SecurityRequirement().addList("bearerAuth"),
                        new SecurityRequirement().addList("oauth2")));
    }
}
