package com.inova.pfms.kafka.event;

import lombok.Data;

import java.util.Date;

@Data
public class OtpEvent {
    private String otpId;
    private String otp;
    private String email;
    private Date date;

    public OtpEvent(String otpId, String otp, String email, Date date) {
        this.otpId = otpId;
        this.otp = otp;
        this.email = email;
        this.date = date;
    }
}
