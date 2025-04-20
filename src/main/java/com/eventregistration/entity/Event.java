package com.eventregistration.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import com.eventregistration.constant.EventStyle;
import com.eventregistration.constant.SeasonalTheme;
import com.eventregistration.constant.ThemeMode;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Event")
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseAuthor {

    String title;
    String description;
    String coverImage;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String location;

    @Builder.Default
    boolean isOnline = false;

    @ManyToOne
    @JoinColumn(name = "CalendarId", nullable = true)
    Calendar calendar;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventAttendee> attendees;

    String eventColor;
    String fontStyle;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    ThemeMode themeMode = ThemeMode.LIGHT;

    @Enumerated(EnumType.STRING)
    EventStyle style;

    @Enumerated(EnumType.STRING)
    SeasonalTheme seasonalTheme;

    @Builder.Default
    boolean requiresApproval = false;
}
