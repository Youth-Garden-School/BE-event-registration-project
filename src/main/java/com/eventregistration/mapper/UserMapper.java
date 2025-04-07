package com.eventregistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convert email string to UserEmail entity
     *
     * @param email Email address
     * @param user User entity to associate with the email
     * @return UserEmail entity
     */
    @Mapping(target = "email", source = "email")
    @Mapping(target = "user", source = "user")
    UserEmail toUserEmail(String email, User user);

    @Mapping(target = "email", expression = "java(user.getEmails().stream().filter(UserEmail::isPrimary).findFirst().map(UserEmail::getEmail).orElse(null))")
    UserResponse toUserResponse(User user);
}
