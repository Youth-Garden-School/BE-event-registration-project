package com.eventregistration.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventregistration.dto.ApiResponse;
import com.eventregistration.dto.request.CalendarCreationRequest;
import com.eventregistration.dto.request.CalendarUpdateRequest;
import com.eventregistration.dto.response.CalendarResponse;
import com.eventregistration.dto.response.EventResponse;
import com.eventregistration.service.CalendarService;
import com.eventregistration.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/calendars")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Calendar Controller", description = "APIs for calendar management")
public class CalendarController {

    CalendarService calendarService;
    EventService eventService;

    @PostMapping
    @Operation(summary = "Create a new calendar", description = "Create a new calendar for the authenticated user")
    public ApiResponse<CalendarResponse> createCalendar(
            @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CalendarCreationRequest request) {

        String username = jwt.getClaimAsString("username");
        log.info("Creating calendar for user: {}", username);

        CalendarResponse response = calendarService.createCalendar(request, username);

        return ApiResponse.<CalendarResponse>builder()
                .message("Calendar created successfully")
                .result(response)
                .build();
    }

    @GetMapping
    @Operation(summary = "Get user calendars", description = "Get all calendars for the authenticated user")
    public ApiResponse<List<CalendarResponse>> getUserCalendars() {


        List<CalendarResponse> calendars = calendarService.getUserCalendars();

        return ApiResponse.<List<CalendarResponse>>builder()
                .message("Calendars retrieved successfully")
                .result(calendars)
                .build();
    }

    @GetMapping("/{calendarId}")
    @Operation(summary = "Get calendar by ID", description = "Get a specific calendar by ID for the authenticated user")
    public ApiResponse<CalendarResponse> getCalendarById(@PathVariable UUID calendarId) {

        log.info("Getting calendar {} for user: {}", calendarId);

        CalendarResponse calendar = calendarService.getUserCalendarById(calendarId);

        return ApiResponse.<CalendarResponse>builder()
                .message("Calendar retrieved successfully")
                .result(calendar)
                .build();
    }

    @PutMapping("/{calendarId}")
    @Operation(summary = "Update calendar", description = "Update a specific calendar by ID for the authenticated user")
    public ApiResponse<CalendarResponse> updateCalendar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID calendarId,
            @Valid @RequestBody CalendarUpdateRequest request) {

        String username = jwt.getClaimAsString("username");
        log.info("Updating calendar {} for user: {}", calendarId, username);

        CalendarResponse updatedCalendar = calendarService.updateCalendar(calendarId, request, username);

        return ApiResponse.<CalendarResponse>builder()
                .message("Calendar updated successfully")
                .result(updatedCalendar)
                .build();
    }

    @DeleteMapping("/{calendarId}")
    @Operation(summary = "Delete calendar", description = "Delete a specific calendar by ID for the authenticated user")
    public ApiResponse<Void> deleteCalendar(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID calendarId) {

        String username = jwt.getClaimAsString("username");
        log.info("Deleting calendar {} for user: {}", calendarId, username);

        calendarService.deleteCalendar(calendarId, username);

        return ApiResponse.<Void>builder()
                .message("Calendar deleted successfully")
                .build();
    }

    @PostMapping("/{calendarId}/events")
    @Operation(
            summary = "Add multiple events to calendar",
            description = "Add multiple existing events to a specific calendar for the authenticated user")
    public ApiResponse<List<EventResponse>> addEventsToCalendar(
            @AuthenticationPrincipal Jwt jwt, @PathVariable UUID calendarId, @RequestBody List<UUID> eventIds) {

        String username = jwt.getClaimAsString("username");
        log.info("Adding {} events to calendar {} for user: {}", eventIds.size(), calendarId, username);

        calendarService.addEventsToCalendar(calendarId, eventIds, username);

        return ApiResponse.<List<EventResponse>>builder()
                .message("Events added to calendar successfully")
                .build();
    }

    @PostMapping("/{calendarId}/events/{eventId}")
    @Operation(
            summary = "Add existing event to calendar",
            description = "Add an existing event to a specific calendar for the authenticated user")
    public ApiResponse<EventResponse> addEventToCalendar(
            @AuthenticationPrincipal Jwt jwt, @PathVariable UUID calendarId, @PathVariable UUID eventId) {

        String username = jwt.getClaimAsString("username");
        log.info("Adding event {} to calendar {} for user: {}", eventId, calendarId, username);

        EventResponse response = calendarService.addEventToCalendar(calendarId, eventId, username);

        return ApiResponse.<EventResponse>builder()
                .message("Event added to calendar successfully")
                .result(response)
                .build();
    }
}
