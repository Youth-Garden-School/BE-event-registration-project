package com.eventregistration.service.implement;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.dto.request.CalendarCreationRequest;
import com.eventregistration.dto.response.CalendarResponse;
import com.eventregistration.entity.Calendar;
import com.eventregistration.entity.User;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.mapper.CalendarMapper;
import com.eventregistration.repository.CalendarRepository;
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
    
    @Override
    @Transactional
    public CalendarResponse createCalendar(CalendarCreationRequest request, String username) {
        log.info("Creating calendar for user: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        Calendar calendar = Calendar.builder()
                .name(request.name())
                .color(request.color())
                .user(user)
                .build();
        
        Calendar savedCalendar = calendarRepository.save(calendar);
        log.info("Calendar created with ID: {}", savedCalendar.getId());
        
        return calendarMapper.toResponse(savedCalendar);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CalendarResponse> getUserCalendars(String username) {
        log.info("Fetching calendars for user: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        List<Calendar> calendars = calendarRepository.findByUserId(user.getId());
        
        return calendars.stream()
                .map(calendarMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CalendarResponse getUserCalendarById(UUID calendarId, String username) {
        log.info("Fetching calendar {} for user: {}", calendarId, username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        Calendar calendar = calendarRepository.findByIdAndUserId(calendarId, user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CALENDAR_NOT_FOUND));
        
        return calendarMapper.toResponse(calendar);
    }
}