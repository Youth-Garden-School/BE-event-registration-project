package com.eventregistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.eventregistration.dto.request.EmailSignupRequest;
import com.eventregistration.dto.request.VerifyOtpRequest;
import com.eventregistration.dto.response.EmailSignupResponse;
import com.eventregistration.entity.UserEmail;

/**
 * Mapper for authentication-related DTOs and entities
 */
@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    /**
     * Maps UserEmail entity to EmailSignupResponse
     */
    @Mapping(source = "email", target = "email")
    @Mapping(target = "expiresIn", ignore = true) // This is set in the service
    EmailSignupResponse toEmailSignupResponse(UserEmail userEmail);

    /**
     * Maps VerifyOtpRequest to UserEmail entity
     */
    @Mapping(source = "email", target = "email")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isPrimary", constant = "true")
    @Mapping(target = "isVerified", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEmail toUserEmail(VerifyOtpRequest request);

    /**
     * Maps EmailSignupRequest to UserEmail entity
     */
    @Mapping(source = "email", target = "email")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isPrimary", constant = "false")
    @Mapping(target = "isVerified", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEmail toUserEmail(EmailSignupRequest request);
}
