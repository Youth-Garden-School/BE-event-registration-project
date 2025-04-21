package com.eventregistration.service.implement;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.constant.AttendeeStatus;
import com.eventregistration.dto.request.EventRegistrationRequest;
import com.eventregistration.dto.response.EventRegistrationResponse;
import com.eventregistration.entity.Event;
import com.eventregistration.entity.EventAttendee;
import com.eventregistration.entity.User;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.mapper.EventAttendeeMapper;
import com.eventregistration.mapper.PageMapper;
import com.eventregistration.repository.EventAttendeeRepository;
import com.eventregistration.repository.EventRepository;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.EmailService;
import com.eventregistration.service.EventRegistrationService;
import com.eventregistration.shared.dto.response.PaginationInfo;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EventRegistrationServiceImpl implements EventRegistrationService {

    EventRepository eventRepository;
    UserRepository userRepository;
    EventAttendeeRepository eventAttendeeRepository;
    EventAttendeeMapper eventAttendeeMapper;
    EmailService emailService;
    PageMapper pageMapper;

    // registerForEvent method remains unchanged
    @Override
    @Transactional
    public EventRegistrationResponse registerForEvent(UUID eventId, EventRegistrationRequest request, String username) {
        // Implementation remains the same
        log.info("Registering for event: {} by user: {}", eventId, username);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_FOUND));

        AttendeeStatus initialStatus = event.isRequiresApproval() ? AttendeeStatus.PENDING : AttendeeStatus.CONFIRMED;

        EventAttendee.EventAttendeeBuilder attendeeBuilder =
                EventAttendee.builder().event(event).status(initialStatus).notes(request.notes());

        if (username != null) {
            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            if (eventAttendeeRepository.existsByEventIdAndUserId(eventId, user.getId())) {
                throw new AppException(ErrorCode.USER_ALREADY_REGISTERED);
            }

            attendeeBuilder.user(user);

            String email = user.getEmails().stream()
                    .filter(e -> e.isPrimary())
                    .findFirst()
                    .map(e -> e.getEmail())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND));

            attendeeBuilder.email(email);
        } else {
            if (request.guestEmail() == null || request.guestEmail().isBlank()) {
                throw new AppException(ErrorCode.GUEST_EMAIL_REQUIRED);
            }

            if (eventAttendeeRepository.existsByEventIdAndEmail(eventId, request.guestEmail())) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_REGISTERED);
            }

            attendeeBuilder.email(request.guestEmail());
        }

        EventAttendee attendee = attendeeBuilder.build();
        EventAttendee savedAttendee = eventAttendeeRepository.save(attendee);

        // Send confirmation email with ICS attachment
        try {
            emailService.sendEventRegistrationEmail(event, savedAttendee);
            log.info("Registration confirmation email sent to: {}", savedAttendee.getEmail());
        } catch (Exception e) {
            log.error("Failed to send registration email", e);
        }

        return eventAttendeeMapper.toRegistrationResponse(savedAttendee);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationInfo<EventRegistrationResponse> getEventRegistrations(
            UUID eventId, String username, Pageable pageable) {
        log.info("Getting registrations for event: {}", eventId);

        // Verify event exists and user has access
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_FOUND));

        // Check if user is the event owner
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Check if user is the creator of the event
        if (!event.getCreatedBy().equals(user.getId())) {
            throw new AppException(ErrorCode.EVENT_UNAUTHORIZED_ACCESS);
        }

        Page<EventAttendee> attendees = eventAttendeeRepository.findByEventId(eventId, pageable);
        Page<EventRegistrationResponse> responsePage = attendees.map(eventAttendeeMapper::toRegistrationResponse);

        return pageMapper.toPaginationInfo(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationInfo<EventRegistrationResponse> getUserRegistrations(String username, Pageable pageable) {
        log.info("Getting registrations for user: {}", username);

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Page<EventAttendee> attendees = eventAttendeeRepository.findByUserIdWithoutAttendees(user.getId(), pageable);
        Page<EventRegistrationResponse> responsePage = attendees.map(eventAttendeeMapper::toRegistrationResponse);

        return pageMapper.toPaginationInfo(responsePage);
    }

    // cancelRegistration method remains unchanged
    @Override
    @Transactional
    public void cancelRegistration(UUID registrationId, String username) {
        log.info("Cancelling registration: {} by user: {}", registrationId, username);

        EventAttendee attendee = eventAttendeeRepository
                .findById(registrationId)
                .orElseThrow(() -> new AppException(ErrorCode.REGISTRATION_NOT_FOUND));

        // Check if user is authorized to cancel
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // User can cancel if they are the attendee or the event owner
        boolean isAttendee =
                attendee.getUser() != null && attendee.getUser().getId().equals(user.getId());
        boolean isEventOwner = attendee.getEvent().getCreatedBy().equals(user.getId());

        if (!isAttendee && !isEventOwner) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        attendee.setStatus(AttendeeStatus.CANCELLED);
        EventAttendee updatedAttendee = eventAttendeeRepository.save(attendee);
    }
}
