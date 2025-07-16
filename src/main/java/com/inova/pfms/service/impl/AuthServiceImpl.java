package com.inova.pfms.service.impl;

import static com.inova.pfms.constants.LogMessages.*;

import java.sql.Timestamp;
import java.util.UUID;

import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.exception.ObjectCreationFailedException;
import com.inova.pfms.exception.ObjectUpdateFailedException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inova.pfms.dto.request.AuthRequest;
import com.inova.pfms.dto.request.ForgetPasswordDTO;
import com.inova.pfms.dto.request.ProceedOtpVerificationDTO;
import com.inova.pfms.dto.response.AuthResponse;
import com.inova.pfms.dto.response.OtpPendingResponseDTO;
import com.inova.pfms.dto.response.UserDTO;
import com.inova.pfms.entity.OtpRequest;
import com.inova.pfms.entity.User;
import com.inova.pfms.enums.OtpActionEnum;
import com.inova.pfms.enums.OtpStatusEnum;
import com.inova.pfms.enums.UserRoles;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.kafka.publisher.OtpEventPublisher;
import com.inova.pfms.repository.OtpRequestRepository;
import com.inova.pfms.repository.UserRepository;
import com.inova.pfms.service.AuthService;
import com.inova.pfms.util.CommonMethod;
import com.inova.pfms.util.JwtUtil;
import com.inova.pfms.util.OTPUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String INVALID_CREDENTIALS_MSG = "Invalid email or password";
    private final OtpRequestRepository otpRepository;
    private final OtpEventPublisher otpEventPublisher;

    
    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
                           OtpRequestRepository otpRepository, OtpEventPublisher otpEventPublisher) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.otpRepository = otpRepository;
        this.otpEventPublisher = otpEventPublisher;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        log.info(AUTHENTICATION_CALLED, request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MSG));

        if (!user.isActive() || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_MSG);
        }
        String role = user.isAdmin() ? UserRoles.ADMIN.getCode() :  UserRoles.REG_USER.getCode();
        String token = jwtUtil.generateToken(user.getEmail(), role);
        log.info(AUTHENTICATION_SUCCESS, request.getEmail());
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public SuccessResponseDTO forgetPassword(ForgetPasswordDTO request) throws JsonProcessingException {
        try {
            log.info(FORGET_PASSWORD_CALLED, request.getEmail());
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ObjectDoesNotExistException(request.getEmail(), EMAIL_NOT_EXISTS));

            String otp = OTPUtil.generateOtp();
            ObjectMapper mapper = new ObjectMapper();
            request.setUserId(user.getId());
            request.setNewPassword(passwordEncoder.encode(request.getNewPassword()));
            String jsonData = mapper.writeValueAsString(request);

            OtpRequest otpRequest = OtpRequest.builder()
                    .id(UUID.randomUUID().toString())
                    .email(request.getEmail())
                    .action(OtpActionEnum.FORGET_PASSWORD.getCode())
                    .otp(otp)
                    .data(jsonData)
                    .status(OtpStatusEnum.PENDING.getCode())
                    .generatedOn(new Timestamp(System.currentTimeMillis()))
                    .attempts(1)
                    .build();
            otpRequest = otpRepository.save(otpRequest);

            log.info(OTP_EVENT_PUBLISHER_CALLED, request.getEmail());
            otpEventPublisher.createOtpEvent(otpRequest.getId(), otpRequest.getOtp(), request.getEmail(), otpRequest.getGeneratedOn());
            log.info(OTP_EVENT_PUBLISHER_COMPLETED, request.getEmail());

            OtpPendingResponseDTO responseDTO = OtpPendingResponseDTO.builder()
                    .uniqueId(otpRequest.getId())
                    .generatedOn(otpRequest.getGeneratedOn())
                    .build();

            return CommonMethod.getSuccessResponse(
                    "New user verification OTP sent successfully",
                    responseDTO
            );
        } catch (ObjectNotFoundException | JsonProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectCreationFailedException("Failed to send OTP");
        }
    }

    @Override
    @Transactional
    public SuccessResponseDTO verifyOtpAndChangePassword(ProceedOtpVerificationDTO requestDTO) throws JsonProcessingException {
        try {
            log.info(VERIFY_OTP_AND_CHANGE_PASSWORD_CALLED, requestDTO.getEmail());
            OtpRequest otpRequest = otpRepository.findById(requestDTO.getOtpId())
                    .orElseThrow(() -> new ObjectDoesNotExistException(requestDTO.getOtpId(), OTP_REQUEST_NOT_FOUND));
            if (validateOtp(requestDTO.getOtpId(), requestDTO.getOtp(), requestDTO.getEmail())) {
                otpRequest.setStatus(OtpStatusEnum.VALID.getCode());
                otpRepository.save(otpRequest);

                ForgetPasswordDTO forgetPasswordDTO = CommonMethod.getObjectMapper().readValue(otpRequest.getData(), ForgetPasswordDTO.class);
                User user = userRepository.findById(forgetPasswordDTO.getUserId())
                        .orElseThrow(() -> new ObjectDoesNotExistException(forgetPasswordDTO.getUserId(), USER_NOT_FOUND));
                user.setPassword(forgetPasswordDTO.getNewPassword());
                UserDTO response = convertToDTO(userRepository.save(user));
                log.info(PASSWORD_CHANGED_SUCCESS, requestDTO.getEmail());
                return CommonMethod.getSuccessResponse(
                        "User password changed successfully",
                        response
                );
            } else {
                log.info(INVALID_CHANGE_PASSWORD_OTP, requestDTO.getOtp(), requestDTO.getEmail());
                otpRequest.setStatus(OtpStatusEnum.INVALID.getCode());
                otpRepository.save(otpRequest);
                throw new IllegalArgumentException("Invalid OTP");
            }
        } catch (IllegalArgumentException | JsonProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectUpdateFailedException("Failed to verify OTP");
        }
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .active(user.isActive())
                .admin(user.isAdmin())
                .createdAt(user.getCreatedAt())
                .activatedAt(user.getActivatedAt())
                .deactivatedAt(user.getDeactivatedAt())
                .activatedById(user.getActivatedBy() != null ? user.getActivatedBy().getId() : null)
                .deactivatedById(user.getDeactivatedBy() != null ? user.getDeactivatedBy().getId() : null)
                .build();
    }

    private boolean validateOtp(String otpId, String otp, String email) {
        log.info(VALIDATE_OTP_CALLED, otpId, email);
        OtpRequest otpRequest = otpRepository.findFirstByStatusAndActionOrderByGeneratedOnDesc(OtpStatusEnum.PENDING.getCode(), OtpActionEnum.FORGET_PASSWORD.getCode())
                .orElseThrow(() -> new ObjectDoesNotExistException(otpId, NO_PENDING_OTP_REQUESTS));
        if (otpRequest.getId().equalsIgnoreCase(otpId) && otpRequest.getEmail().equalsIgnoreCase(email)) {
            log.warn(VALID_OTP, email, otpId);
            return true;
        } else {
            log.warn(INVALID_OTP, email, otpId);
            return false;
        }
    }
}
