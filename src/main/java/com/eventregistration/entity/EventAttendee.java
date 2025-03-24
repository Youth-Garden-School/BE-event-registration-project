package com.eventregistration.entity;

import com.eventregistration.constant.AttendeeStatus;
import com.eventregistration.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "eventAttendees")
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