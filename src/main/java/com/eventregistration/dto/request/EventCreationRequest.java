package com.eventregistration.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.eventregistration.constant.EventStyle;
import com.eventregistration.constant.SeasonalTheme;
import com.eventregistration.constant.ThemeMode;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating a new event")
public record EventCreationRequest(
        @NotBlank @Schema(description = "Event title", example = "Tech Conference 2023", required = true) String title,
        @Schema(description = "Event description", example = "Annual technology conference with industry experts")
                String description,
        @Schema(description = "URL to event cover image", example = "https://example.com/images/tech-conf.jpg")
                String coverImage,
        @NotNull @Schema(description = "Event start time", example = "2023-12-01T09:00:00", required = true)
                LocalDateTime startTime,
        @NotNull @Schema(description = "Event end time", example = "2023-12-01T17:00:00", required = true)
                LocalDateTime endTime,
        @Schema(description = "Physical location of the event", example = "Convention Center, 123 Main St")
                String location,
        @Schema(description = "Whether the event is online", example = "true") boolean isOnline,
        @Schema(description = "ID of the calendar this event belongs to", example = "1") UUID calendarId,
        @Schema(description = "Event color in hex format", example = "#FF5733") String eventColor,
        @Schema(description = "Font style for event display", example = "Arial") String fontStyle,
        @Schema(description = "Theme mode for event display", example = "LIGHT") ThemeMode themeMode,
        @Schema(description = "Style of the event", example = "MODERN") EventStyle style,
        @Schema(description = "Seasonal theme for the event", example = "CHRISTMAS") SeasonalTheme seasonalTheme,
        @Schema(description = "Whether approval is required to join the event", example = "false")
                boolean requiresApproval) {}
