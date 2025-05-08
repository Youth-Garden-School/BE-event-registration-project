package com.eventregistration.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventregistration.dto.ApiResponse;
import com.eventregistration.dto.request.EventRegistrationRequest;
import com.eventregistration.dto.response.EventRegistrationResponse;
import com.eventregistration.service.EventRegistrationService;
import com.eventregistration.shared.dto.request.PageRequest;
import com.eventregistration.shared.dto.response.PaginationInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Event Registration Controller", description = "APIs for event registration management")
public class EventRegistrationController {

    EventRegistrationService eventRegistrationService;

    @PostMapping("/events/{eventId}/registrations")
    @Operation(summary = "Register for an event", description = "Register the authenticated user for an event")
    public ApiResponse<EventRegistrationResponse> registerForEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId,
            @Valid @RequestBody EventRegistrationRequest request) {

        String username = jwt != null ? jwt.getClaimAsString("username") : null;
        log.info("Registering for event: {} by user: {}", eventId, username);

        EventRegistrationResponse response = eventRegistrationService.registerForEvent(eventId, request, username);

        return ApiResponse.<EventRegistrationResponse>builder()
                .message("Event registration successful")
                .result(response)
                .build();
    }

    @PostMapping("/events/{eventId}/registrations/guest")
    @Operation(
            summary = "Guest registration for an event",
            description = "Register a guest (non-authenticated user) for an event")
    public ApiResponse<EventRegistrationResponse> registerGuestForEvent(
            @PathVariable UUID eventId, @Valid @RequestBody EventRegistrationRequest request) {

        log.info("Registering guest for event: {} with email: {}", eventId, request.guestEmail());

        EventRegistrationResponse response = eventRegistrationService.registerForEvent(eventId, request, null);

        return ApiResponse.<EventRegistrationResponse>builder()
                .message("Guest registration successful")
                .result(response)
                .build();
    }

    @PostMapping("/events/{eventId}/registrations/get")
    @Operation(
            summary = "Get event registrations",
            description = "Get all registrations for an event (event owner only)")
    public ApiResponse<PaginationInfo<EventRegistrationResponse>> getEventRegistrations(
            @AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId, @Valid @RequestBody PageRequest pageRequest) {

        String username = jwt.getClaimAsString("username");
        log.info("Getting registrations for event: {} by user: {}", eventId, username);

        PaginationInfo<EventRegistrationResponse> paginationInfo =
                eventRegistrationService.getEventRegistrations(eventId, username, pageRequest.toPageable());

        return ApiResponse.<PaginationInfo<EventRegistrationResponse>>builder()
                .message("Event registrations retrieved successfully")
                .result(paginationInfo)
                .build();
    }

    @PostMapping("/registrations/me")
    @Operation(
            summary = "Get user registrations",
            description = "Get all events the authenticated user is registered for")
    public ApiResponse<PaginationInfo<EventRegistrationResponse>> getUserRegistrations(
            @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody PageRequest pageRequest) {

        String username = jwt.getClaimAsString("username");
        log.info("Getting registrations for user: {}", username);

        PaginationInfo<EventRegistrationResponse> paginationInfo =
                eventRegistrationService.getUserRegistrations(username, pageRequest.toPageable());

        return ApiResponse.<PaginationInfo<EventRegistrationResponse>>builder()
                .message("User registrations retrieved successfully")
                .result(paginationInfo)
                .build();
    }

    @PutMapping("/registrations/{registrationId}/cancel")
    @Operation(
            summary = "Cancel registration",
            description = "Cancel a registration (can be done by the attendee or event owner)")
    public ApiResponse<EventRegistrationResponse> cancelRegistration(
            @AuthenticationPrincipal Jwt jwt, @PathVariable UUID registrationId) {

        String username = jwt.getClaimAsString("username");
        log.info("Cancelling registration: {} by user: {}", registrationId, username);

        eventRegistrationService.cancelRegistration(registrationId, username);

        return ApiResponse.<EventRegistrationResponse>builder()
                .message("Registration cancelled successfully")
                .build();
    }
}
