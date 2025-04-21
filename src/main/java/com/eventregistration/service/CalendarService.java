package com.eventregistration.service;

import java.util.List;
import java.util.UUID;

import com.eventregistration.dto.request.CalendarCreationRequest;
import com.eventregistration.dto.request.CalendarUpdateRequest;
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

    /**
     * Update a calendar
     *
     * @param calendarId Calendar ID to update
     * @param request Calendar update request
     * @param username Username of the calendar owner
     * @return Updated calendar details
     */
    CalendarResponse updateCalendar(UUID calendarId, CalendarUpdateRequest request, String username);

    /**
     * Delete a calendar
     *
     * @param calendarId Calendar ID to delete
     * @param username Username of the calendar owner
     */
    void deleteCalendar(UUID calendarId, String username);
}
