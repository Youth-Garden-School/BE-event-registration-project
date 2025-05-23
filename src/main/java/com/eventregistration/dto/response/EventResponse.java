package com.eventregistration.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.eventregistration.constant.EventStyle;
import com.eventregistration.constant.SeasonalTheme;
import com.eventregistration.constant.ThemeMode;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing complete event information")
public record EventResponse(
        @Schema(description = "Event ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,
        @Schema(description = "Event title", example = "Tech Conference 2023") String title,
        @Schema(description = "Event description", example = "Annual technology conference") String description,
        @Schema(description = "Cover image URL", example = "https://example.com/images/event.jpg") String coverImage,
        @Schema(description = "Event start time", example = "2023-12-01T09:00:00") LocalDateTime startTime,
        @Schema(description = "Event end time", example = "2023-12-01T17:00:00") LocalDateTime endTime,
        @Schema(description = "Event location", example = "Convention Center") String location,
        @Schema(description = "Whether the event is online", example = "true") boolean isOnline,
        @Schema(description = "Event color", example = "#FF5733") String eventColor,
        @Schema(description = "Font style", example = "Arial") String fontStyle,
        @Schema(description = "Theme mode", example = "LIGHT", enumAsRef = true) ThemeMode themeMode,
        @Schema(description = "Event style", example = "MODERN", enumAsRef = true) EventStyle style,
        @Schema(description = "Seasonal theme", example = "CHRISTMAS", enumAsRef = true) SeasonalTheme seasonalTheme,
        @Schema(description = "Whether approval is required to join", example = "false") boolean requiresApproval,
        @Schema(description = "List of attendees") List<UserResponse> attendees,
        @Schema(description = "Event category", example = "Technology") String category,
        @Schema(description = "Creation timestamp", example = "2023-11-15T10:30:00") LocalDateTime createdAt,
        @Schema(description = "Last update timestamp", example = "2023-11-16T14:45:00") LocalDateTime updatedAt,
        @Schema(description = "ID of user who created this event", example = "123e4567-e89b-12d3-a456-426614174000")
                UUID createdBy,
        @Schema(
                        description = "ID of user who last updated this event",
                        example = "123e4567-e89b-12d3-a456-426614174000")
                UUID updatedBy) {}
