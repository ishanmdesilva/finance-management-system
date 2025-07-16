package com.inova.pfms.controller;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inova.pfms.dto.request.ProceedOtpVerificationDTO;
import com.inova.pfms.dto.request.UpdateUserDTO;
import com.inova.pfms.dto.request.CommonStatusUpdateRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inova.pfms.dto.request.CreateUserDTO;
import com.inova.pfms.dto.response.UserDTO;
import com.inova.pfms.service.UserService;

/**
 * Controller for managing user-related operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'REG_USER')")
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Initiates user registration.
     *
     * @param userDTO  - user request
     * @return ResponseEntity
     * @throws JsonProcessingException if JSON processing fails
     */
    @PostMapping(value = "/register-initiate")
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserDTO userDTO) throws JsonProcessingException {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    /**
     * Verifies OTP and completes user registration.
     *
     * @param userDTO  - otp verification request
     * @return ResponseEntity
     * @throws JsonProcessingException if JSON processing fails
     */
    @PostMapping(value = "/register-verification")
    public ResponseEntity<Object> validateOtpAndCreateUser(@Valid @RequestBody ProceedOtpVerificationDTO userDTO) throws JsonProcessingException {
        return ResponseEntity.ok(userService.validateOtpAndCreateUser(userDTO));
    }

    /**
     * Updates user details.
     *
     * @param id             the user ID
     * @param userRequestDTO the updated user details
     * @return  ResponseEntity
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'REG_USER')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRequestDTO, id));
    }

    /**
     * Retrieves the details of the logged-in user.
     *
     * @return the user details
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'REG_USER')")
    @GetMapping(value = "/current")
    public ResponseEntity<Object> getUserDetails() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserDetails());
    }

    /**
     * Updates the status of a user.
     *
     * @param id         the user ID
     * @param requestDTO the status update request
     * @return a response indicating the update status
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}/status")
    public ResponseEntity<Object> updateUserStatus(@PathVariable String id, @RequestBody CommonStatusUpdateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserStatus(id, requestDTO));
    }
}
