package com.eventregistration.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.eventregistration.constant.AttendeeStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for event registration")
public record EventRegistrationResponse(
        @Schema(description = "Registration ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        
        @Schema(description = "Event details")
        EventResponse event,
        
        @Schema(description = "User details (null for guest registrations)")
        UserResponse user,
        
        @Schema(description = "Email address used for registration", example = "user@example.com")
        String email,
        
        @Schema(description = "Registration status", example = "CONFIRMED")
        AttendeeStatus status,
        
        @Schema(description = "Registration timestamp", example = "2023-11-15T10:30:00")
        LocalDateTime registeredAt,
        
        @Schema(description = "Notes provided during registration", example = "Looking forward to the event!")
        String notes) {}