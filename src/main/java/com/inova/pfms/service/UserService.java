package com.inova.pfms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inova.pfms.dto.request.CommonStatusUpdateRequestDTO;
import com.inova.pfms.dto.request.CreateUserDTO;
import com.inova.pfms.dto.request.ProceedOtpVerificationDTO;
import com.inova.pfms.dto.request.UpdateUserDTO;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.dto.response.UserDTO;

import java.util.List;

public interface UserService {
    /**
     * Retrieves all users.
     *
     * @return List<UserDTO> a list of all users
     */
    List<UserDTO> getAllUsers();

    /**
     * Initiates user registration.
     *
     * @param userDTO  - user request
     * @return SuccessResponseDTO
     * @throws JsonProcessingException if JSON processing fails
     */
    SuccessResponseDTO createUser(CreateUserDTO userDTO) throws JsonProcessingException;

    /**
     * Verifies OTP and completes user registration.
     *
     * @param requestDTO  - otp verification request
     * @return SuccessResponseDTO
     * @throws JsonProcessingException if JSON processing fails
     */
    SuccessResponseDTO validateOtpAndCreateUser(ProceedOtpVerificationDTO requestDTO) throws JsonProcessingException;

    /**
     * Updates user details.
     *
     * @param id             the user ID
     * @param userDTO the updated user details
     * @return  SuccessResponseDTO
     */
    SuccessResponseDTO updateUser(UpdateUserDTO userDTO, String id);

    /**
     * Retrieves the details of the logged-in user.
     *
     * @return the user details
     */
    SuccessResponseDTO getUserDetails();

    /**
     * Updates the status of a user.
     *
     * @param userId         the user ID
     * @param requestDTO the status update request
     * @return a response indicating the update status
     */
    SuccessResponseDTO changeUserStatus(String userId, CommonStatusUpdateRequestDTO requestDTO);
}
