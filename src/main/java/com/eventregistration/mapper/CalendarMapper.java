package com.eventregistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.eventregistration.dto.request.CalendarCreationRequest;
import com.eventregistration.dto.request.CalendarUpdateRequest;
import com.eventregistration.dto.response.CalendarResponse;
import com.eventregistration.entity.Calendar;
import com.eventregistration.entity.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class, EventMapper.class})
public interface CalendarMapper {

    @Mapping(target = "events", source = "events")
    CalendarResponse toResponse(Calendar calendar);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "coverImage", source = "request.coverImage")
    @Mapping(target = "avatarImage", source = "request.avatarImage")
    Calendar toEntity(CalendarCreationRequest request, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "user", ignore = true)
    Calendar updateFromRequest(CalendarUpdateRequest request, @MappingTarget Calendar calendar);
}
