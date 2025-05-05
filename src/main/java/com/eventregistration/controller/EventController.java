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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import com.eventregistration.dto.ApiResponse;
import com.eventregistration.dto.request.EventCreationRequest;
import com.eventregistration.dto.request.EventUpdateRequest;
import com.eventregistration.dto.response.EventResponse;
import com.eventregistration.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Event Controller", description = "APIs for event management")
public class EventController {

    EventService eventService;

    @PostMapping
    @Operation(summary = "Create a new event", description = "Create a new event for the authenticated user")
    public ApiResponse<EventResponse> createEvent(
            @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody EventCreationRequest request) {

        String username = jwt.getClaimAsString("username");
        log.info("Creating event for user: {}", username);

        EventResponse response = eventService.createEvent(request, username);

        return ApiResponse.<EventResponse>builder()
                .message("Event created successfully")
                .result(response)
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all user events", description = "Get all events created by the authenticated user")
    public ApiResponse<List<EventResponse>> getUserEvents(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("username");
        log.info("Fetching events for user: {}", username);

        List<EventResponse> events = eventService.getUserEvents(username);

        return ApiResponse.<List<EventResponse>>builder()
                .message("Events fetched successfully")
                .result(events)
                .build();
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get event by ID", description = "Get a specific event by ID for the authenticated user")
    public ApiResponse<EventResponse> getEventById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {

        String username = jwt.getClaimAsString("username");
        log.info("Fetching event {} for user: {}", eventId, username);

        EventResponse event = eventService.getUserEventById(eventId, username);

        return ApiResponse.<EventResponse>builder()
                .message("Event fetched successfully")
                .result(event)
                .build();
    }

    @PutMapping("/{eventId}")
    @Operation(summary = "Update event", description = "Update an existing event for the authenticated user")
    public ApiResponse<EventResponse> updateEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId,
            @Valid @RequestBody EventUpdateRequest request) {

        String username = jwt.getClaimAsString("username");
        log.info("Updating event {} for user: {}", eventId, username);

        EventResponse updatedEvent = eventService.updateEvent(eventId, request, username);

        return ApiResponse.<EventResponse>builder()
                .message("Event updated successfully")
                .result(updatedEvent)
                .build();
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete event", description = "Delete an event for the authenticated user")
    public ApiResponse<Void> deleteEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {

        String username = jwt.getClaimAsString("username");
        log.info("Deleting event {} for user: {}", eventId, username);

        eventService.deleteEvent(eventId, username);

        return ApiResponse.<Void>builder().message("Event deleted successfully").build();
    }

    @GetMapping("/category")
    @Operation(summary = "Get events by category", description = "Get all events with the specified category")
    public ApiResponse<List<EventResponse>> getEventsByCategory(
            @AuthenticationPrincipal Jwt jwt, @RequestParam String category) {

        String decodedCategory = UriUtils.decode(category, "UTF-8");

        String username = jwt.getClaimAsString("username");
        log.info("Fetching events with category: {} for user: {}", decodedCategory, username);

        List<EventResponse> events = eventService.getEventsByCategory(decodedCategory);

        return ApiResponse.<List<EventResponse>>builder()
                .message("Events with category '" + decodedCategory + "' fetched successfully")
                .result(events)
                .build();
    }
}
