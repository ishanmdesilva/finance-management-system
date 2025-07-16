package com.inova.pfms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inova.pfms.dto.request.CommonStatusUpdateRequestDTO;
import com.inova.pfms.dto.request.CreateUserDTO;
import com.inova.pfms.dto.request.ProceedOtpVerificationDTO;
import com.inova.pfms.dto.request.UpdateUserDTO;
import com.inova.pfms.dto.response.OtpPendingResponseDTO;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.dto.response.UserDTO;
import com.inova.pfms.entity.OtpRequest;
import com.inova.pfms.entity.User;
import com.inova.pfms.enums.OtpActionEnum;
import com.inova.pfms.enums.OtpStatusEnum;
import com.inova.pfms.exception.ObjectCreationFailedException;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.exception.ObjectUpdateFailedException;
import com.inova.pfms.kafka.publisher.OtpEventPublisher;
import com.inova.pfms.repository.OtpRequestRepository;
import com.inova.pfms.repository.UserRepository;
import com.inova.pfms.service.UserService;
import com.inova.pfms.util.CommonMethod;
import com.inova.pfms.util.OTPUtil;
import com.inova.pfms.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static com.inova.pfms.constants.LogMessages.CHANGE_USER_STATUS_CALLED;
import static com.inova.pfms.constants.LogMessages.CHANGE_USER_STATUS_SUCCESS;
import static com.inova.pfms.constants.LogMessages.CREATE_USER_CALLED;
import static com.inova.pfms.constants.LogMessages.GET_USER_DETAILS_CALLED;
import static com.inova.pfms.constants.LogMessages.GET_USER_DETAILS_SUCCESS;
import static com.inova.pfms.constants.LogMessages.NO_PENDING_OTP;
import static com.inova.pfms.constants.LogMessages.OTP_NOT_FOUND;
import static com.inova.pfms.constants.LogMessages.OTP_PUBLISHER_CALLED;
import static com.inova.pfms.constants.LogMessages.OTP_PUBLISHER_COMPLETED;
import static com.inova.pfms.constants.LogMessages.OTP_SENT_SUCCESSFULLY;
import static com.inova.pfms.constants.LogMessages.UPDATE_USER_CALLED;
import static com.inova.pfms.constants.LogMessages.UPDATE_USER_SUCCESS;
import static com.inova.pfms.constants.LogMessages.USER_CREATED_SUCCESSFULLY;
import static com.inova.pfms.constants.LogMessages.USER_NOT_FOUND;
import static com.inova.pfms.constants.LogMessages.VALIDATE_OTP_CALLED;
import static com.inova.pfms.constants.LogMessages.VALIDATE_OTP_CREATE_USER_CALLED;

@Service
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OtpRequestRepository otpRepository;
    private PasswordEncoder passwordEncoder;
    private UserUtil userUtil;
    private OtpEventPublisher otpEventPublisher;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional
    public SuccessResponseDTO createUser(CreateUserDTO dto) throws JsonProcessingException {
        try {
            log.info(CREATE_USER_CALLED, dto.getEmail());
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }

            String otp = OTPUtil.generateOtp();
            ObjectMapper mapper = new ObjectMapper();
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
            String jsonData = mapper.writeValueAsString(dto);

            OtpRequest otpRequest = OtpRequest.builder()
                    .id(UUID.randomUUID().toString())
                    .email(dto.getEmail())
                    .action(OtpActionEnum.NEW_USER.getCode())
                    .otp(otp)
                    .data(jsonData)
                    .status(OtpStatusEnum.PENDING.getCode())
                    .generatedOn(new Timestamp(System.currentTimeMillis()))
                    .attempts(1)
                    .build();
            otpRequest = otpRepository.save(otpRequest);

            log.info(OTP_PUBLISHER_CALLED, dto.getEmail());
            otpEventPublisher.createOtpEvent(otpRequest.getId(), otpRequest.getOtp(), dto.getEmail(), otpRequest.getGeneratedOn());
            log.info(OTP_PUBLISHER_COMPLETED, dto.getEmail());

            OtpPendingResponseDTO responseDTO = OtpPendingResponseDTO.builder()
                    .uniqueId(otpRequest.getId())
                    .generatedOn(otpRequest.getGeneratedOn())
                    .build();
            log.info(OTP_SENT_SUCCESSFULLY, dto.getEmail());
            return CommonMethod.getSuccessResponse("New user verification OTP sent successfully", responseDTO);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectCreationFailedException("Failed to create user " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public SuccessResponseDTO validateOtpAndCreateUser(ProceedOtpVerificationDTO requestDTO) throws JsonProcessingException {
        try {
            log.info(VALIDATE_OTP_CREATE_USER_CALLED, requestDTO.getEmail());
            OtpRequest otpRequest = otpRepository.findById(requestDTO.getOtpId())
                    .orElseThrow(() -> new ObjectDoesNotExistException(requestDTO.getOtpId(), OTP_NOT_FOUND));

            if (validateOtp(requestDTO.getOtpId(), requestDTO.getOtp(), requestDTO.getEmail())) {
                otpRequest.setStatus(OtpStatusEnum.VALID.getCode());
                otpRepository.save(otpRequest);

                CreateUserDTO createUserDTO = CommonMethod.getObjectMapper().readValue(otpRequest.getData(), CreateUserDTO.class);
                User user = convertToUser(createUserDTO);
                UserDTO response = convertToDTO(userRepository.save(user));

                log.info(USER_CREATED_SUCCESSFULLY, requestDTO.getEmail());
                return CommonMethod.getSuccessResponse("New user created successfully", response);
            } else {
                otpRequest.setStatus(OtpStatusEnum.INVALID.getCode());
                otpRepository.save(otpRequest);
                throw new IllegalArgumentException("Invalid OTP");
            }
        } catch (IllegalArgumentException | JsonProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectCreationFailedException("Failed to validate OTP " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public SuccessResponseDTO updateUser(UpdateUserDTO userDTO, String userId) {
        try {
            log.info(UPDATE_USER_CALLED, userDTO.getEmail(), userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ObjectDoesNotExistException(userId, USER_NOT_FOUND));

            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());

            log.info(UPDATE_USER_SUCCESS, userDTO.getEmail(), userId);
            return CommonMethod.getSuccessResponse("Regular user updated successfully", convertToDTO(userRepository.save(user)));
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectUpdateFailedException("Failed to update user " + e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO getUserDetails() {
        try {
            log.info(GET_USER_DETAILS_CALLED);
            User user = userUtil.getLoggedInUser();
            UserDTO userDTO = convertToDTO(user);
            log.info(GET_USER_DETAILS_SUCCESS, userDTO.getEmail());
            return CommonMethod.getSuccessResponse("User retrieved successfully", userDTO);
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to find user " , e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO changeUserStatus(String userId, CommonStatusUpdateRequestDTO requestDTO) {
        try {
            log.info(CHANGE_USER_STATUS_CALLED, userId, requestDTO.isActive());
            User loggedInUser = userUtil.getLoggedInUser();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ObjectDoesNotExistException(userId, USER_NOT_FOUND));

            user.setActive(requestDTO.isActive());
            if (user.isActive()) {
                user.setActivatedAt(new Timestamp(System.currentTimeMillis()));
                user.setActivatedBy(loggedInUser);
            } else {
                user.setDeactivatedAt(new Timestamp(System.currentTimeMillis()));
                user.setDeactivatedBy(loggedInUser);
            }

            user = userRepository.save(user);
            log.info(CHANGE_USER_STATUS_SUCCESS, userId, requestDTO.isActive());
            return CommonMethod.getSuccessResponse("Changed user status successfully", convertToDTO(user));
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectUpdateFailedException("Failed to change user status " + e.getMessage());
        }
    }

    private boolean validateOtp(String otpId, String otp, String email) {
        log.info(VALIDATE_OTP_CALLED, otpId, email);
        OtpRequest otpRequest = otpRepository.findFirstByStatusAndActionOrderByGeneratedOnDesc(OtpStatusEnum.PENDING.getCode(), OtpActionEnum.NEW_USER.getCode())
                .orElseThrow(() -> new ObjectDoesNotExistException(otpId, NO_PENDING_OTP));
        return otpRequest.getId().equalsIgnoreCase(otpId) && otpRequest.getEmail().equalsIgnoreCase(email);
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

    private User convertToUser(CreateUserDTO dto) {
        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        return User.builder()
                .id(UUID.randomUUID().toString())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .active(true)
                .admin(false)
                .createdAt(curTime)
                .activatedAt(curTime)
                .build();
    }
}
