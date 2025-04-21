package com.eventregistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.eventregistration.dto.response.CalendarResponse;
import com.eventregistration.entity.Calendar;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CalendarMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    CalendarResponse toResponse(Calendar calendar);
}