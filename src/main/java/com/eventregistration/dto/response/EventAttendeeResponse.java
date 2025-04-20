package com.eventregistration.dto.response;

import java.util.UUID;

import com.eventregistration.constant.AttendeeStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for event attendee information")
public record EventAttendeeResponse(
        @Schema(description = "Attendee ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,
        @Schema(description = "User information", implementation = UserResponse.class) UserResponse user,
        @Schema(description = "Attendee email", example = "attendee@example.com") String email,
        @Schema(description = "Attendee status", example = "CONFIRMED", enumAsRef = true) AttendeeStatus status) {}
