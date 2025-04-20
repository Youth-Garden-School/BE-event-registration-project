package com.eventregistration.service;

import java.util.List;
import java.util.UUID;

import com.eventregistration.dto.request.EventCreationRequest;
import com.eventregistration.dto.request.EventUpdateRequest;
import com.eventregistration.dto.response.EventResponse;

public interface EventService {
    /**
     * Create a new event
     * 
     * @param request Event creation request
     * @param username Username of the authenticated user
     * @return Created event response
     */
    EventResponse createEvent(EventCreationRequest request, String username);
    
    /**
     * Get all events for a user
     * 
     * @param username Username of the authenticated user
     * @return List of events
     */
    List<EventResponse> getUserEvents(String username);
    
    /**
     * Get a specific event by ID for a user
     * 
     * @param eventId Event ID
     * @param username Username of the authenticated user
     * @return Event response
     */
    EventResponse getUserEventById(UUID eventId, String username);
    
    /**
     * Update an existing event
     * 
     * @param eventId Event ID to update
     * @param request Event update request
     * @param username Username of the authenticated user
     * @return Updated event response
     */
    EventResponse updateEvent(UUID eventId, EventUpdateRequest request, String username);
    
    /**
     * Delete an event
     * 
     * @param eventId Event ID to delete
     * @param username Username of the authenticated user
     */
    void deleteEvent(UUID eventId, String username);
}
