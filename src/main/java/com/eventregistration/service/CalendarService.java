package com.eventregistration.service;

import java.util.List;
import java.util.UUID;

import com.eventregistration.dto.request.CalendarCreationRequest;
import com.eventregistration.dto.request.CalendarUpdateRequest;
import com.eventregistration.dto.response.CalendarResponse;
import com.eventregistration.dto.response.EventResponse;

public interface CalendarService {
    CalendarResponse createCalendar(CalendarCreationRequest request, String username);

    List<CalendarResponse> getUserCalendars(String username);

    CalendarResponse getUserCalendarById(UUID calendarId, String username);

    CalendarResponse updateCalendar(UUID calendarId, CalendarUpdateRequest request, String username);

    void deleteCalendar(UUID calendarId, String username);
    
    EventResponse addEventToCalendar(UUID calendarId, UUID eventId, String username);
    
    List<EventResponse> addEventsToCalendar(UUID calendarId, List<UUID> eventIds, String username);
}
