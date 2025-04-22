package com.eventregistration.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing tokens and user information")
public record AuthResponse(
        @Schema(
                        description = "JWT access token for API authorization",
                        example =
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
                String accessToken,
        @Schema(
                        description = "JWT refresh token to obtain new access tokens",
                        example =
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
                String refreshToken,
        @Schema(description = "Flag indicating if this is a newly created user account", example = "false")
                boolean isNewUser,
        @Schema(description = "Detailed user profile information") UserResponse user) {}
