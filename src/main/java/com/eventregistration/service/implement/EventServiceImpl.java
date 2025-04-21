package com.eventregistration.service.implement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.dto.request.EventCreationRequest;
import com.eventregistration.dto.request.EventUpdateRequest;
import com.eventregistration.dto.response.EventResponse;
import com.eventregistration.entity.Calendar;
import com.eventregistration.entity.Event;
import com.eventregistration.entity.User;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.mapper.EventMapper;
import com.eventregistration.repository.CalendarRepository;
import com.eventregistration.repository.EventRepository;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.EmailService;
import com.eventregistration.service.EventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public EventResponse createEvent(EventCreationRequest request, String username) {
        Event.EventBuilder<?, ?> eventBuilder = Event.builder()
                .title(request.title())
                .description(request.description())
                .coverImage(request.coverImage())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .location(request.location())
                .isOnline(request.isOnline())
                .eventColor(request.eventColor())
                .fontStyle(request.fontStyle())
                .themeMode(request.themeMode())
                .style(request.style())
                .seasonalTheme(request.seasonalTheme())
                .requiresApproval(request.requiresApproval());

        if (request.calendarId() != null) {
            Calendar calendar = calendarRepository.findById(request.calendarId())
                    .orElseThrow(() -> {
                        return new AppException(ErrorCode.CALENDAR_NOT_FOUND);
                    });
        
            
            eventBuilder.calendar(calendar);
        }

        Event event = eventBuilder.build();
        Event savedEvent = eventRepository.save(event);
        
        return eventMapper.toResponse(savedEvent);
    }

    @Override
    public List<EventResponse> getUserEvents(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        List<Event> events = eventRepository.findByCreatedBy(user);
        
        return events.stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse getUserEventById(UUID eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_FOUND));
        
        
        return eventMapper.toResponse(event);
    }

    @Override
    @Transactional
    public EventResponse updateEvent(UUID eventId, EventUpdateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_FOUND));
        
        
        if (request.title() != null) event.setTitle(request.title());
        if (request.description() != null) event.setDescription(request.description());
        if (request.coverImage() != null) event.setCoverImage(request.coverImage());
        if (request.startTime() != null) event.setStartTime(request.startTime());
        if (request.endTime() != null) event.setEndTime(request.endTime());
        if (request.location() != null) event.setLocation(request.location());
        if (request.isOnline() != null) event.setOnline(request.isOnline());
        if (request.eventColor() != null) event.setEventColor(request.eventColor());
        if (request.fontStyle() != null) event.setFontStyle(request.fontStyle());
        if (request.themeMode() != null) event.setThemeMode(request.themeMode());
        if (request.style() != null) event.setStyle(request.style());
        if (request.seasonalTheme() != null) event.setSeasonalTheme(request.seasonalTheme());
        if (request.requiresApproval() != null) event.setRequiresApproval(request.requiresApproval());
        
        if (request.calendarId() != null) {
            Calendar calendar = calendarRepository.findById(request.calendarId())
                    .orElseThrow(() -> new AppException(ErrorCode.CALENDAR_NOT_FOUND));
            
            event.setCalendar(calendar);
        }
        
        event.setUpdatedAt(LocalDateTime.now());
        Event updatedEvent = eventRepository.save(event);
        
        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_FOUND));
        
        eventRepository.delete(event);
    }
    
}
