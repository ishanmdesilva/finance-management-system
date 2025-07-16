package com.inova.pfms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserStatus {

    ACTIVE("ACTIVE", "Active"),
    INACTIVE("INACTIVE", "Inactive");

    private String code;
    private String value;

}
