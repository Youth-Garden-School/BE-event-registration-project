package com.eventregistration.service.implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.dto.request.CalendarCreationRequest;
import com.eventregistration.dto.request.CalendarUpdateRequest;
import com.eventregistration.dto.response.CalendarResponse;
import com.eventregistration.dto.response.EventResponse;
import com.eventregistration.entity.Calendar;
import com.eventregistration.entity.Event;
import com.eventregistration.entity.User;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.mapper.CalendarMapper;
import com.eventregistration.mapper.EventMapper;
import com.eventregistration.repository.CalendarRepository;
import com.eventregistration.repository.EventRepository;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.CalendarService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CalendarServiceImpl implements CalendarService {

    CalendarRepository calendarRepository;
    UserRepository userRepository;
    CalendarMapper calendarMapper;
    EventRepository eventRepository;
    EventMapper eventMapper;

    @Override
    @Transactional
    public CalendarResponse createCalendar(CalendarCreationRequest request, String username) {
        log.info("Creating calendar for user: {}", username);

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Calendar calendar = calendarMapper.toEntity(request, user);

        Calendar savedCalendar = calendarRepository.save(calendar);
        log.info("Calendar created with ID: {}", savedCalendar.getId());

        return calendarMapper.toResponse(savedCalendar);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarResponse> getUserCalendars(String username) {
        log.info("Fetching calendars for user: {}", username);

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Calendar> calendars = calendarRepository.findByUserId(user.getId());

        return calendars.stream().map(calendarMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CalendarResponse getUserCalendarById(UUID calendarId, String username) {
        log.info("Fetching calendar {} for user: {}", calendarId, username);

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Calendar calendar = calendarRepository
                .findByIdAndUserId(calendarId, user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CALENDAR_NOT_FOUND));

        return calendarMapper.toResponse(calendar);
    }

    @Override
    @Transactional
    public CalendarResponse updateCalendar(UUID calendarId, CalendarUpdateRequest request, String username) {
        log.info("Updating calendar {} for user: {}", calendarId, username);

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Calendar calendar = calendarRepository
                .findByIdAndUserId(calendarId, user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CALENDAR_NOT_FOUND));

        if (request.name() != null) calendar.setName(request.name());

        if (request.color() != null) calendar.setColor(request.color());

        if (request.coverImage() != null) calendar.setCoverImage(request.coverImage());

        if (request.avatarImage() != null) calendar.setAvatarImage(request.avatarImage());

        if (request.description() != null) calendar.setDescription(request.description());

        calendar.setUpdatedAt(LocalDateTime.now());
        calendar.setUpdatedBy(user.getId());

        Calendar updatedCalendar = calendarRepository.save(calendar);

        log.info("Calendar updated: {}", updatedCalendar.getId());
        return calendarMapper.toResponse(updatedCalendar);
    }

    @Override
    @Transactional
    public void deleteCalendar(UUID calendarId, String username) {
        log.info("Deleting calendar {} for user: {}", calendarId, username);

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Calendar calendar = calendarRepository
                .findByIdAndUserId(calendarId, user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CALENDAR_NOT_FOUND));

        long calendarCount = calendarRepository.countByUserId(user.getId());
        if (calendarCount <= 1) {
            throw new AppException(ErrorCode.CALENDAR_CANNOT_DELETE_LAST);
        }

        calendarRepository.delete(calendar);
        log.info("Calendar deleted: {}", calendarId);
    }

    @Override
    @Transactional
    public EventResponse addEventToCalendar(UUID calendarId, UUID eventId, String username) {
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Calendar calendar = calendarRepository
                .findById(calendarId)
                .orElseThrow(() -> new AppException(ErrorCode.CALENDAR_NOT_FOUND));

        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.CALENDAR_UNAUTHORIZED_ACCESS);
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_FOUND));

        event.setCalendar(calendar);
        event.setUpdatedAt(LocalDateTime.now());
        event.setUpdatedBy(user.getId());

        Event updatedEvent = eventRepository.save(event);

        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    @Transactional
    public List<EventResponse> addEventsToCalendar(UUID calendarId, List<UUID> eventIds, String username) {
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Calendar calendar = calendarRepository
                .findById(calendarId)
                .orElseThrow(() -> new AppException(ErrorCode.CALENDAR_NOT_FOUND));

        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.CALENDAR_UNAUTHORIZED_ACCESS);
        }

        List<Event> updatedEvents = new ArrayList<>();

        for (UUID eventId : eventIds) {
            Event event =
                    eventRepository.findById(eventId).orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_FOUND));

            event.setCalendar(calendar);
            event.setUpdatedAt(LocalDateTime.now());
            event.setUpdatedBy(user.getId());

            updatedEvents.add(event);
        }

        List<Event> savedEvents = eventRepository.saveAll(updatedEvents);

        return savedEvents.stream().map(eventMapper::toResponse).collect(Collectors.toList());
    }
}
