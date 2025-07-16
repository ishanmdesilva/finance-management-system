package com.inova.pfms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inova.pfms.dto.request.AuthRequest;
import com.inova.pfms.dto.request.ForgetPasswordDTO;
import com.inova.pfms.dto.request.ProceedOtpVerificationDTO;
import com.inova.pfms.dto.response.AuthResponse;
import com.inova.pfms.dto.response.SuccessResponseDTO;

public interface AuthService {

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param request -   authentication request
     * @return        -   AuthResponse
     */
    AuthResponse authenticate(AuthRequest request);

    /**
     * Initiates the forget password process by generating and sending an OTP.
     *
     * @param request -   forget password request
     * @return        -   SuccessResponseDTO
     */
    SuccessResponseDTO forgetPassword(ForgetPasswordDTO request) throws JsonProcessingException;

    /**
     * Verifies the OTP and updates the user's password.
     *
     * @param request -   OTP verification request
     * @return        -   SuccessResponseDTO
     */
    SuccessResponseDTO verifyOtpAndChangePassword(ProceedOtpVerificationDTO request) throws JsonProcessingException;
}
