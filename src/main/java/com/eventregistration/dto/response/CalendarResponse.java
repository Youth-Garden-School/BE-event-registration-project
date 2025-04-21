package com.eventregistration.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record CalendarResponse(
        @Schema(description = "Calendar ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,
        @Schema(description = "Calendar name", example = "Work Schedule") String name,
        @Schema(description = "Calendar color in hex format", example = "#4285F4") String color,
        @Schema(description = "User ID who owns this calendar", example = "123e4567-e89b-12d3-a456-426614174000")
                UUID userId,
        @Schema(description = "Username who owns this calendar", example = "johndoe") String username,
        @Schema(description = "Creation timestamp", example = "2023-11-15T10:30:00") LocalDateTime createdAt,
        @Schema(description = "Last update timestamp", example = "2023-11-16T14:45:00") LocalDateTime updatedAt) {}
