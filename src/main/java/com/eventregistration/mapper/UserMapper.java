package com.eventregistration.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {List.class})
public interface UserMapper {

    @Mapping(
            target = "email",
            expression =
                    "java(user.getEmails().stream().filter(UserEmail::isPrimary).findFirst().map(UserEmail::getEmail).orElse(null))")
    UserResponse toUserResponse(User user);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "isPrimary", constant = "true")
    @Mapping(target = "isVerified", constant = "false")
    UserEmail toUserEmail(String email, User user);
}
