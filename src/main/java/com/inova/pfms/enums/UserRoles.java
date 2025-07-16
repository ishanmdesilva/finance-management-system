package com.inova.pfms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
    ADMIN("ADMIN", "Admin"),
    REG_USER("REG_USER", "Regular_user");

    private String code;
    private String description;
}
