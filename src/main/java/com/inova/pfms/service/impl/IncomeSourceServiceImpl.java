package com.inova.pfms.service.impl;

import static com.inova.pfms.constants.LogMessages.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.inova.pfms.dto.response.SuccessResponseDTO;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inova.pfms.dto.request.IncomeSourceRequestDTO;
import com.inova.pfms.dto.response.IncomeSourceResponseDTO;
import com.inova.pfms.entity.IncomeSource;
import com.inova.pfms.entity.User;
import com.inova.pfms.exception.ObjectAlreadyExistsException;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.repository.IncomeSourceRepository;
import com.inova.pfms.service.IncomeSourceService;
import com.inova.pfms.util.CommonMethod;
import com.inova.pfms.util.UserUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class IncomeSourceServiceImpl implements IncomeSourceService {

    private IncomeSourceRepository incomeSourceRepository;
    private UserUtil userUtil;


    @Override
    @Transactional
    public SuccessResponseDTO createIncomeSource(IncomeSourceRequestDTO requestDTO) {
        try {
            log.info(CREATE_INCOME_SOURCE_CALLED);
            User loggedInUser = userUtil.getLoggedInUser();
            Optional<IncomeSource> existingSources = incomeSourceRepository.findByUserAndDeletedFalseAndName(loggedInUser, requestDTO.getName());
            if (existingSources.isPresent()) {
                throw new ObjectAlreadyExistsException(INCOME_SOURCE_ALREADY_EXISTS);
            }
            IncomeSource incomeSource = IncomeSource.builder()
                    .id(UUID.randomUUID().toString())
                    .name(requestDTO.getName())
                    .description(requestDTO.getDescription())
                    .deleted(false)
                    .user(loggedInUser)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            incomeSource = incomeSourceRepository.save(incomeSource);
            log.info(INCOME_SOURCE_CREATED, incomeSource.getId());
            return CommonMethod.getSuccessResponse(
                    "Income source added successfully",
                    convertToResponseDTO(incomeSource)
            );
        } catch (ObjectAlreadyExistsException | ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public SuccessResponseDTO findIncomeSource(String sourceId) {
        try {
            log.info(FIND_INCOME_SOURCE_CALLED, sourceId);
            IncomeSource incomeSource = incomeSourceRepository.findById(sourceId)
                    .orElseThrow(() -> new ObjectDoesNotExistException(sourceId, INCOME_SOURCE_NOT_FOUND));
            log.info(FIND_INCOME_SOURCE_SUCCESS, sourceId);
            return CommonMethod.getSuccessResponse(
                    "income source details retrieved successfully",
                    convertToResponseDTO(incomeSource)
            );
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public SuccessResponseDTO findAllIncomeSource() {
        try {
            log.info(FIND_ALL_INCOME_SOURCES_CALLED);
            User loggedInUser = userUtil.getLoggedInUser();
            List<IncomeSourceResponseDTO> requestDTOList = incomeSourceRepository.findByUserAndDeletedFalseOrderByCreatedAtDesc(loggedInUser)
                    .stream()
                    .map(this::convertToResponseDTO)
                    .toList();

            log.info(FIND_ALL_INCOME_SOURCES_SUCCESS);
            return CommonMethod.getSuccessResponse(
                    "Income source list retrieved successfully",
                    requestDTOList
            );
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public SuccessResponseDTO updateIncomeSource(IncomeSourceRequestDTO requestDTO, String sourceId) {
        try {
            log.info(UPDATE_INCOME_SOURCE_CALLED, sourceId);
            User loggedInUser = userUtil.getLoggedInUser();
            IncomeSource incomeSource = incomeSourceRepository.findById(sourceId)
                    .orElseThrow(() -> new ObjectDoesNotExistException(sourceId, INCOME_SOURCE_NOT_FOUND));
            if (!incomeSource.getUser().getId().equalsIgnoreCase(loggedInUser.getId()) || incomeSource.isDeleted()) {
                throw new ObjectAlreadyExistsException(UNAUTHORIZED_UPDATE_ATTEMPT);
            }
            incomeSource.setName(requestDTO.getName());
            incomeSource.setDescription(requestDTO.getDescription());
            incomeSource.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            incomeSource = incomeSourceRepository.save(incomeSource);

            log.info(INCOME_SOURCE_UPDATED, incomeSource.getId());
            return CommonMethod.getSuccessResponse(
                    "Income source updated successfully",
                    convertToResponseDTO(incomeSource)
            );
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public SuccessResponseDTO deleteIncomeSource(String sourceId) {
        try {
            log.info(DELETE_INCOME_SOURCE_CALLED, sourceId);
            IncomeSource incomeSource = incomeSourceRepository.findById(sourceId)
                    .orElseThrow(() -> new ObjectDoesNotExistException(sourceId, INCOME_SOURCE_NOT_FOUND));
            incomeSource.setDeleted(true);
            incomeSource.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            incomeSource = incomeSourceRepository.save(incomeSource);
            log.info(INCOME_SOURCE_DELETED, incomeSource.getId());
            return CommonMethod.getSuccessResponse(
                    "Income source's status changed to deleted successfully",
                    convertToResponseDTO(incomeSource)
            );
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private IncomeSourceResponseDTO convertToResponseDTO(IncomeSource incomeSource) {
        return IncomeSourceResponseDTO.builder()
                .id(incomeSource.getId())
                .name(incomeSource.getName())
                .description(incomeSource.getDescription())
                .userId(incomeSource.getUser().getId())
                .deleted(incomeSource.isDeleted())
                .createdAt(incomeSource.getCreatedAt())
                .build();
    }
}
