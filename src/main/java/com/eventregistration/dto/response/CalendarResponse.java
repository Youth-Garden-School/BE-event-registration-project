package com.eventregistration.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for calendar information")
public record CalendarResponse(
        @Schema(description = "Calendar ID") UUID id,
        @Schema(description = "Calendar name") String name,
        @Schema(description = "Calendar color") String color,
        @Schema(description = "Calendar cover image URL") String coverImage,
        @Schema(description = "Calendar avatar image URL") String avatarImage,
        @Schema(description = "Calendar detailed description") String description,
        @Schema(description = "Events in this calendar") List<EventResponse> events,
        @Schema(description = "Creation timestamp") LocalDateTime createdAt,
        @Schema(description = "Last update timestamp") LocalDateTime updatedAt,
        @Schema(description = "ID of user who created this calendar", example = "123e4567-e89b-12d3-a456-426614174000")
                UUID createdBy,
        @Schema(
                        description = "ID of user who last updated this calendar",
                        example = "123e4567-e89b-12d3-a456-426614174000")
                UUID updatedBy) {}
