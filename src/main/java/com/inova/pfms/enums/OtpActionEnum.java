package com.inova.pfms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OtpActionEnum {
    NEW_USER("NEW_USER", "user_registration"),
    FORGET_PASSWORD("FORGET_PASSWORD", "forget_password");

    private String code;
    private String description;
}
