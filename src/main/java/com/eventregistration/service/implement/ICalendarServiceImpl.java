package com.eventregistration.service.implement;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eventregistration.entity.Event;
import com.eventregistration.entity.EventAttendee;
import com.eventregistration.entity.User;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.ICalendarService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ICalendarServiceImpl implements ICalendarService {

    UserRepository userRepository;

    @Override
    public byte[] generateIcsCalendar(Event event, EventAttendee attendee) {
        try {
            // Create a calendar
            Calendar calendar = new Calendar();
            calendar.getProperties().add(new ProdId("-//Event Registration System//iCal4j 1.0//EN"));
            calendar.getProperties().add(Version.VERSION_2_0);
            calendar.getProperties().add(CalScale.GREGORIAN);
            calendar.getProperties().add(Method.REQUEST);

            // Get timezone
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone timezone = registry.getTimeZone("Asia/Ho_Chi_Minh");

            // Create event start and end times
            DateTime start = new DateTime(Date.from(event.getStartTime().atZone(ZoneId.systemDefault()).toInstant()));
            start.setTimeZone(timezone);
            
            DateTime end = new DateTime(Date.from(event.getEndTime().atZone(ZoneId.systemDefault()).toInstant()));
            end.setTimeZone(timezone);

            // Create the event
            VEvent vEvent = new VEvent(start, end, event.getTitle());
            
            // Add event properties
            vEvent.getProperties().add(new Uid(UUID.randomUUID().toString()));
            
            if (event.getDescription() != null && !event.getDescription().isEmpty()) {
                vEvent.getProperties().add(new Description(event.getDescription()));
            }
            
            if (event.getLocation() != null && !event.getLocation().isEmpty()) {
                vEvent.getProperties().add(new Location(event.getLocation()));
            }
            
            // Add organizer
            String organizerEmail = "noreply@eventregistration.com";
            String organizerName = "Event Registration System";
            
            if (event.getCreatedBy() != null) {
                // Try to find the user who created the event
                User eventCreator = userRepository.findById(event.getCreatedBy()).orElse(null);
                
                if (eventCreator != null) {
                    organizerName = eventCreator.getUsername();
                    
                    // Try to get primary email
                    if (eventCreator.getEmails() != null && !eventCreator.getEmails().isEmpty()) {
                        organizerEmail = eventCreator.getEmails().stream()
                                .filter(e -> e.isPrimary())
                                .findFirst()
                                .map(e -> e.getEmail())
                                .orElse("noreply@eventregistration.com");
                    }
                }
            }
            
            Organizer organizer = new Organizer("mailto:" + organizerEmail);
            organizer.getParameters().add(new Cn(organizerName));
            vEvent.getProperties().add(organizer);
            
            // Add attendee
            Attendee icsAttendee = new Attendee("mailto:" + attendee.getEmail());
            icsAttendee.getParameters().add(Role.REQ_PARTICIPANT);
            
            String attendeeName = attendee.getEmail();
            if (attendee.getUser() != null) {
                attendeeName = attendee.getUser().getUsername();
            }
            
            icsAttendee.getParameters().add(new Cn(attendeeName));
            vEvent.getProperties().add(icsAttendee);
            
            // Add event to calendar
            calendar.getComponents().add(vEvent);
            
            // Write calendar to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, baos);
            
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating ICS calendar", e);
            throw new RuntimeException("Failed to generate ICS calendar", e);
        }
    }
}