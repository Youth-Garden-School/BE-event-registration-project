package com.eventregistration.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.eventregistration.dto.request.EventRegistrationRequest;
import com.eventregistration.dto.response.EventRegistrationResponse;
import com.eventregistration.shared.dto.response.PaginationInfo;

public interface EventRegistrationService {
    EventRegistrationResponse registerForEvent(UUID eventId, EventRegistrationRequest request, String username);
    
    PaginationInfo<EventRegistrationResponse> getEventRegistrations(UUID eventId, String username, Pageable pageable);
    
    PaginationInfo<EventRegistrationResponse> getUserRegistrations(String username, Pageable pageable);
    
    void cancelRegistration(UUID registrationId, String username);
}