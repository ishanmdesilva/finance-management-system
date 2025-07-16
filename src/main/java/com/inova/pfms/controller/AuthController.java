package com.inova.pfms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inova.pfms.dto.request.ForgetPasswordDTO;
import com.inova.pfms.dto.request.ProceedOtpVerificationDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inova.pfms.dto.request.AuthRequest;
import com.inova.pfms.dto.response.AuthResponse;
import com.inova.pfms.service.AuthService;

/**
 * Controller for handling authentication-related operations.
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param request -   authentication request
     * @return        -   ResponseEntity with AuthResponse
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid  @RequestBody AuthRequest request) {
    	AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Initiates the forget password process by generating and sending an OTP.
     *
     * @param forgetPasswordDTO -   forgetPassword request
     * @return                  -   ResponseEntity
     */
    @PostMapping(value = "/forget-password-initiate")
    public ResponseEntity<Object> forgetPassword(@Valid @RequestBody ForgetPasswordDTO forgetPasswordDTO) throws JsonProcessingException {
        return ResponseEntity.ok(authService.forgetPassword(forgetPasswordDTO));
    }

    /**
     * Verifies the OTP and updates the user's password.
     *
     * @param verificationDTO -   OTP verification request
     * @return                -   ResponseEntity
     */
    @PostMapping(value = "/forget-password-verification")
    public ResponseEntity<Object> verifyOtpAndChangePassword(@Valid @RequestBody ProceedOtpVerificationDTO verificationDTO) throws JsonProcessingException {
        return ResponseEntity.ok(authService.verifyOtpAndChangePassword(verificationDTO));
    }
}
