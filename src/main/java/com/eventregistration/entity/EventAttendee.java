package com.eventregistration.entity;

import jakarta.persistence.*;

import com.eventregistration.constant.AttendeeStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "EventAttendee")
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class EventAttendee extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "EventId", nullable = false)
    Event event;

    @ManyToOne
    @JoinColumn(name = "UserId")
    User user;

    String email;

    @Enumerated(EnumType.STRING)
    AttendeeStatus status = AttendeeStatus.PENDING;
}
