package com.inova.pfms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OtpStatusEnum {
    PENDING("PENDING", "Pending"),
    VALID("VALID", "Valid"),
    INVALID("INVALID", "Invalid");

    private String code;
    private String description;
}
