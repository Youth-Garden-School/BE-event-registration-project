package com.eventregistration.service;

import java.util.List;
import java.util.UUID;

import com.eventregistration.dto.request.CalendarCreationRequest;
import com.eventregistration.dto.response.CalendarResponse;

public interface CalendarService {
    /**
     * Create a new calendar for a user
     *
     * @param request Calendar creation request
     * @param username Username of the calendar owner
     * @return Created calendar details
     */
    CalendarResponse createCalendar(CalendarCreationRequest request, String username);
    
    /**
     * Get all calendars for a user
     *
     * @param username Username of the calendar owner
     * @return List of user's calendars
     */
    List<CalendarResponse> getUserCalendars(String username);
    
    /**
     * Get a specific calendar by ID for a user
     *
     * @param calendarId Calendar ID to retrieve
     * @param username Username of the calendar owner
     * @return Calendar details if found
     */
    CalendarResponse getUserCalendarById(UUID calendarId, String username);
}