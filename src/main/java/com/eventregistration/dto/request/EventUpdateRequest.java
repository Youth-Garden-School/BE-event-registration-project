package com.eventregistration.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.eventregistration.constant.EventStyle;
import com.eventregistration.constant.SeasonalTheme;
import com.eventregistration.constant.ThemeMode;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for updating an existing event")
public record EventUpdateRequest(
        @Schema(description = "Event title", example = "Updated Tech Conference 2023") String title,
        @Schema(description = "Event description", example = "Updated technology conference with industry experts")
                String description,
        @Schema(description = "URL to event cover image", example = "https://example.com/images/updated-tech-conf.jpg")
                String coverImage,
        @Schema(description = "Event start time", example = "2023-12-01T10:00:00")
                LocalDateTime startTime,
        @Schema(description = "Event end time", example = "2023-12-01T18:00:00")
                LocalDateTime endTime,
        @Schema(description = "Physical location of the event", example = "Updated Convention Center, 123 Main St")
                String location,
        @Schema(description = "Whether the event is online", example = "true") Boolean isOnline,
        @Schema(description = "ID of the calendar this event belongs to", example = "1") UUID calendarId,
        @Schema(description = "Event color in hex format", example = "#3366FF") String eventColor,
        @Schema(description = "Font style for event display", example = "Roboto") String fontStyle,
        @Schema(description = "Theme mode for event display", example = "DARK") ThemeMode themeMode,
        @Schema(description = "Style of the event", example = "MINIMAL") EventStyle style,
        @Schema(description = "Seasonal theme for the event", example = "HALLOWEEN") SeasonalTheme seasonalTheme,
        @Schema(description = "Whether approval is required to join the event", example = "true")
                Boolean requiresApproval) {}