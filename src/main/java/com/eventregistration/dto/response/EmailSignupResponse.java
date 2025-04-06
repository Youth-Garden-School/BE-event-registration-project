package com.eventregistration.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailSignupResponse {
    @Schema(description = "Email address where OTP was sent", example = "user@example.com")
    String email;

    @Schema(description = "Expiration time of OTP in seconds", example = "300")
    int expiresIn;
}
