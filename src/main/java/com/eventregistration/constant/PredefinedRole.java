package com.eventregistration.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PredefinedRole {
    USER_ROLE("USER"),
    ADMIN_ROLE("ADMIN"),
    ;
    String name;

    PredefinedRole(String name) {
        this.name = name;
    }
}
