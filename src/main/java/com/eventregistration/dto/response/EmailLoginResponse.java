package com.eventregistration.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmailLoginResponse(
        @Schema(description = "Email address where OTP was sent", example = "user@example.com") String email,
        @Schema(description = "Whether this is a new user", example = "false") boolean isNewUser,
        @Schema(description = "Whether the user has a password set", example = "true") boolean hasPassword,
        @Schema(description = "Expiration time of OTP in seconds", example = "300") int expirationSeconds) {}
