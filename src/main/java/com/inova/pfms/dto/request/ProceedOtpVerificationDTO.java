package com.inova.pfms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProceedOtpVerificationDTO {

    @NotBlank(message = "OTP ID is required")
    private String otpId;
    @NotBlank(message = "OTP is required")
    private String otp;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
