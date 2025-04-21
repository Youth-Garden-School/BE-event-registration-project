package com.eventregistration.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,
        @Schema(description = "Username", example = "johndoe") String username,
        @Schema(description = "First name", example = "John") String firstName,
        @Schema(description = "Last name", example = "Doe") String lastName,
        @Schema(description = "Email address", example = "john.doe@example.com") String email,
        @Schema(description = "URL to user's avatar image", example = "https://example.com/avatars/johndoe.jpg")
                String avatarUrl,
        @Schema(description = "User biography", example = "Software developer with 5 years of experience") String bio,
        @Schema(description = "Account creation timestamp", example = "2023-11-15T10:30:00") LocalDateTime createdAt,
        @Schema(description = "Last update timestamp", example = "2023-11-16T14:45:00") LocalDateTime updatedAt) {}
