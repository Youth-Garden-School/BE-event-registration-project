package com.eventregistration.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(description = "JWT access token") String accessToken,
        @Schema(description = "JWT refresh token") String refreshToken,
        @Schema(description = "Whether this is a new user", example = "false") boolean isNewUser,
        UserResponse user) {}
