package com.eventregistration.shared.dto.response;

import java.util.UUID;

public record BaseAuthorResponse(UUID createdBy, UUID updatedBy) {}
