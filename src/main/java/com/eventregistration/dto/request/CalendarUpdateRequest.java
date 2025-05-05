package com.eventregistration.dto.request;

import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for updating an existing calendar")
public record CalendarUpdateRequest(
        @Size(min = 3, max = 100, message = "CALENDAR_NAME_LENGTH")
                @Schema(description = "Calendar name", example = "Work Schedule")
                String name,
        @Schema(description = "Calendar color in hex format", example = "#4285F4") String color,
        @Schema(description = "Calendar cover image URL", example = "https://example.com/cover.jpg") String coverImage,
        @Schema(description = "Calendar avatar image URL", example = "https://example.com/avatar.jpg")
                String avatarImage,
        @Schema(
                        description = "Calendar detailed description",
                        example = "This calendar contains all my work-related events and meetings")
                String description) {}
