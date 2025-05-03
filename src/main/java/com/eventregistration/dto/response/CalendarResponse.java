package com.eventregistration.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing calendar information")
public record CalendarResponse(
        @Schema(description = "Calendar ID", example = "123e4567-e89b-12d3-a456-426614174000")
                UUID id,
        @Schema(description = "Calendar name", example = "Work Schedule")
                String name,
        @Schema(description = "Calendar color in hex format", example = "#4285F4")
                String color,
        @Schema(description = "Calendar cover image URL", example = "https://example.com/cover.jpg")
                String coverImage,
        @Schema(description = "Calendar avatar image URL", example = "https://example.com/avatar.jpg")
                String avatarImage,
        @Schema(description = "User ID who owns this calendar", example = "123e4567-e89b-12d3-a456-426614174000")
                UUID userId,
        @Schema(description = "List of events in this calendar")
                List<EventResponse> events,
        @Schema(description = "Creation timestamp")
                LocalDateTime createdAt,
        @Schema(description = "Last update timestamp")
                LocalDateTime updatedAt) {}
