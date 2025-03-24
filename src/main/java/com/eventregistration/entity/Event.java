package com.eventregistration.entity;

import com.eventregistration.constant.EventStyle;
import com.eventregistration.constant.SeasonalTheme;
import com.eventregistration.constant.ThemeMode;
import com.eventregistration.entity.common.BaseAuthor;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
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
    boolean isOnline = false;

    @ManyToOne
    @JoinColumn(name = "CalendarId", nullable = false)
    Calendar calendar;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventAttendee> attendees;

    String eventColor;
    String fontStyle;

    @Enumerated(EnumType.STRING)
    ThemeMode themeMode = ThemeMode.LIGHT;

    @Enumerated(EnumType.STRING)
    EventStyle style;

    @Enumerated(EnumType.STRING)
    SeasonalTheme seasonalTheme;

    boolean requiresApproval = false;
}