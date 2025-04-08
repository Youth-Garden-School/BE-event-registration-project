package com.eventregistration.mapper;

import java.util.List;
import java.util.Set;

import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;
import com.eventregistration.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "email", expression = "java(user.getEmails().stream().filter(UserEmail::isPrimary).findFirst().map(UserEmail::getEmail).orElse(null))")
    UserResponse toUserResponse(User user);


    @Mapping(target = "email", source = "email")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "isPrimary", constant = "true")
    @Mapping(target = "isVerified", constant = "false")
    UserEmail toUserEmail(String email, User user);
}
