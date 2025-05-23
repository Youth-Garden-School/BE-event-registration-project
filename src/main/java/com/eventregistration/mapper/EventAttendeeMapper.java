package com.eventregistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.eventregistration.dto.response.EventRegistrationResponse;
import com.eventregistration.entity.EventAttendee;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {EventMapper.class, UserMapper.class})
public interface EventAttendeeMapper {

    @Mapping(target = "event", source = "event")
    @Mapping(target = "registeredAt", source = "createdAt")
    @Mapping(target = "user", source = "user")
    EventRegistrationResponse toRegistrationResponse(EventAttendee eventAttendee);
}
