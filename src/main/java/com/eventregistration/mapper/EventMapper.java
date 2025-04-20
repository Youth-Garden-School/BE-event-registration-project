package com.eventregistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eventregistration.dto.response.EventAttendeeResponse;
import com.eventregistration.dto.response.EventResponse;
import com.eventregistration.entity.Event;
import com.eventregistration.entity.EventAttendee;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventResponse toResponse(Event event);

    @Mapping(target = "user", source = "user")
    EventAttendeeResponse toAttendeeResponse(EventAttendee eventAttendee);
}
