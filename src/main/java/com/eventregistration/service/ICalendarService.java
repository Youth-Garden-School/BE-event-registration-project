package com.eventregistration.service;

import com.eventregistration.entity.Event;
import com.eventregistration.entity.EventAttendee;

public interface ICalendarService {
    
    /**
     * Generate an ICS calendar file for an event
     * 
     * @param event The event to generate the calendar for
     * @param attendee The attendee who registered for the event
     * @return Byte array containing the ICS file
     */
    byte[] generateIcsCalendar(Event event, EventAttendee attendee);
}