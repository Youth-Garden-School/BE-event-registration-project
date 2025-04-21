package com.eventregistration.dto.request;

import jakarta.validation.constraints.Email;

import io.swagger.v3.oas.annotations.media.Schema;

public record EventRegistrationRequest(
        @Schema(description = "Additional notes or message from attendee", example = "Looking forward to the event!")
                String notes,
        @Email(message = "INVALID_EMAIL_FORMAT")
                @Schema(
                        description =
                                "Email address for guest registration (optional, only for non-authenticated users)",
                        example = "guest@example.com")
                String guestEmail) {}
