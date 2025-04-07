package com.eventregistration.dto.response;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID userId,
        @Schema(description = "Username", example = "johndoe") String username,
        @Schema(description = "Email address", example = "user@example.com") String email,
        @Schema(description = "JWT access token") String accessToken,
        @Schema(description = "JWT refresh token") String refreshToken,
        @Schema(description = "Whether this is a new user", example = "false") boolean isNewUser,
        UserResponse user) {}
