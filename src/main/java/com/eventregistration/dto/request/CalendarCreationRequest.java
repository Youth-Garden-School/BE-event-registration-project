package com.eventregistration.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating a new calendar")
public record CalendarCreationRequest(
        @NotBlank(message = "CALENDAR_NAME_REQUIRED")
                @Size(min = 3, max = 100, message = "CALENDAR_NAME_LENGTH")
                @Schema(description = "Calendar name", example = "Work Schedule", required = true)
                String name,
        @NotBlank(message = "CALENDAR_COLOR_REQUIRED")
                @Schema(description = "Calendar color in hex format", example = "#4285F4", required = true)
                String color,
        @Schema(description = "Calendar cover image URL", example = "https://example.com/cover.jpg") String coverImage,
        @Schema(description = "Calendar avatar image URL", example = "https://example.com/avatar.jpg")
                String avatarImage,
        @Schema(
                        description = "Calendar detailed description",
                        example = "This calendar contains all my work-related events and meetings")
                String description) {}
