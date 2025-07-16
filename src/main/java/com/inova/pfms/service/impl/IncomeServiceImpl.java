package com.inova.pfms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inova.pfms.dto.request.AddIncomeDto;
import com.inova.pfms.dto.response.IncomeDTO;
import com.inova.pfms.dto.response.IncomeTotalAmountResponse;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.entity.Budget;
import com.inova.pfms.entity.Income;
import com.inova.pfms.entity.IncomeSource;
import com.inova.pfms.entity.User;
import com.inova.pfms.exception.ObjectCreationFailedException;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.exception.ObjectUpdateFailedException;
import com.inova.pfms.kafka.publisher.IncomeEventPublisher;
import com.inova.pfms.repository.IncomeRepository;
import com.inova.pfms.repository.IncomeSourceRepository;
import com.inova.pfms.service.CommonService;
import com.inova.pfms.service.IncomeService;
import com.inova.pfms.util.CommonMethod;
import com.inova.pfms.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static com.inova.pfms.constants.LogMessages.ADD_INCOME_CALLED;
import static com.inova.pfms.constants.LogMessages.DELETE_INCOME_CALLED;
import static com.inova.pfms.constants.LogMessages.DELETE_INCOME_SUCCESS;
import static com.inova.pfms.constants.LogMessages.FIND_ALL_INCOME_CALLED;
import static com.inova.pfms.constants.LogMessages.FIND_ALL_INCOME_SUCCESS;
import static com.inova.pfms.constants.LogMessages.FIND_INCOME_CALLED;
import static com.inova.pfms.constants.LogMessages.FIND_INCOME_SUCCESS;
import static com.inova.pfms.constants.LogMessages.GET_INCOME_TOTAL_AMOUNT;
import static com.inova.pfms.constants.LogMessages.GET_TOTAL_INCOME_AMOUNT;
import static com.inova.pfms.constants.LogMessages.INCOME_ADDED_SUCCESS;
import static com.inova.pfms.constants.LogMessages.INCOME_EVENT_PUBLISHED;
import static com.inova.pfms.constants.LogMessages.INCOME_EVENT_PUBLISH_ERROR;
import static com.inova.pfms.constants.LogMessages.INCOME_UPDATED_SUCCESS;
import static com.inova.pfms.constants.LogMessages.INVALID_INCOME_RECORD;
import static com.inova.pfms.constants.LogMessages.UPDATE_INCOME_CALLED;
import static com.inova.pfms.util.CommonConstant.INCOME_RECORD_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeSourceRepository incomeSourceRepository;
    private final IncomeEventPublisher incomeEventPublisher;
    private final UserUtil userUtil;
    private final CommonService commonService;


    @Override
    @Transactional
    public SuccessResponseDTO addIncome(AddIncomeDto dto) throws JsonProcessingException {
        try {
            log.info(ADD_INCOME_CALLED);
            User loggedInUser = userUtil.getLoggedInUser();
            IncomeSource incomeSource = null;

            if (dto.getIncomeSourceId() != null) {
                incomeSource = incomeSourceRepository.findById(dto.getIncomeSourceId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid incomeSource ID"));
            }

            Income income = Income.builder()
                    .id(UUID.randomUUID().toString())
                    .user(loggedInUser)
                    .amount(dto.getAmount())
                    .incomeSource(incomeSource)
                    .incomeDate(dto.getIncomeDate())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .deleted(false)
                    .build();

            Income savedIncome = incomeRepository.save(income);

            try {
                incomeEventPublisher.publishIncomeCreatedEvent(
                        loggedInUser.getId(),
                        savedIncome.getId(),
                        savedIncome.getAmount(),
                        savedIncome.getIncomeDate()
                );
                log.info(INCOME_EVENT_PUBLISHED);
            } catch (Exception e) {
                log.error(INCOME_EVENT_PUBLISH_ERROR, e.getMessage());
                throw e;
            }

            log.info(INCOME_ADDED_SUCCESS, income.getId());
            return CommonMethod.getSuccessResponse(
                    "Income record added successfully",
                    convertToDTO(savedIncome)
            );

        } catch (Exception e) {
            throw new ObjectCreationFailedException("Failed to create income " + e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO updateIncome(AddIncomeDto dto, String id) {
        try {
            log.info(UPDATE_INCOME_CALLED, id);
            User loggeInUser = userUtil.getLoggedInUser();
            Income income = incomeRepository.findById(id)
                    .orElseThrow(() -> new ObjectDoesNotExistException(id, INCOME_RECORD_NOT_FOUND));
            if (!income.getUser().getId().equalsIgnoreCase(loggeInUser.getId())) {
                log.warn(INVALID_INCOME_RECORD, id);
                throw new IllegalArgumentException("User is not allowed to update this income record");
            }

            IncomeSource incomeSource = null;

            if (dto.getIncomeSourceId() != null) {
                incomeSource = incomeSourceRepository.findById(dto.getIncomeSourceId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid incomeSource ID"));
            }

            income.setIncomeDate(dto.getIncomeDate());
            income.setAmount(dto.getAmount());
            income.setIncomeSource(incomeSource);
            income.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            income = incomeRepository.save(income);

            log.info(INCOME_UPDATED_SUCCESS, income.getId());
            return CommonMethod.getSuccessResponse(
                    "Income record updated successfully",
                    convertToDTO(income)
            );
        } catch (ObjectNotFoundException | IllegalArgumentException | ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectUpdateFailedException("Failed to update income " + e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO findIncome(String id) {
        try {
            log.info(FIND_INCOME_CALLED, id);
            Income income = incomeRepository.findById(id)
                    .orElseThrow(() -> new ObjectDoesNotExistException(id, INCOME_RECORD_NOT_FOUND));
            log.info(FIND_INCOME_SUCCESS, income.getId());
            return CommonMethod.getSuccessResponse(
                    "Income record retrieved successfully",
                    convertToDTO(income)
            );
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to find income record by ID ", e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO findAllIncome() {
        try {
            log.info(FIND_ALL_INCOME_CALLED);
            User loggeInUser = userUtil.getLoggedInUser();
            List<IncomeDTO> incomeList = incomeRepository.findByUserAndDeletedFalseOrderByIncomeDateDesc(loggeInUser)
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
            log.info(FIND_ALL_INCOME_SUCCESS);
            return CommonMethod.getSuccessResponse(
                    "Income record list retrieved successfully",
                    incomeList
            );
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to find income ", e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO deleteIncome(String id) {
        try {
            log.info(DELETE_INCOME_CALLED, id);
            Income income = incomeRepository.findById(id)
                    .orElseThrow(() -> new ObjectDoesNotExistException(id, INCOME_RECORD_NOT_FOUND));
            income.setDeleted(true);
            income.setDeletedAt(new Timestamp(System.currentTimeMillis()));
            income = incomeRepository.save(income);

            log.info(DELETE_INCOME_SUCCESS, id);
            return CommonMethod.getSuccessResponse(
                    "Income record deleted successfully",
                    convertToDTO(income)
            );
        } catch (Exception e) {
            throw new ObjectUpdateFailedException("Failed to delete income record " + e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO getTotalIncomesAmount() {
        log.info(GET_INCOME_TOTAL_AMOUNT);

        // Get active budget
        Budget activeBudget = commonService.getActiveBudget();

        // Calculate total income amount
        BigDecimal totalAmount = incomeRepository.findByDeletedFalseAndUserAndIncomeDateBetween(userUtil.getLoggedInUser(), activeBudget.getStartDate(), activeBudget.getEndDate())
                .stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // build response
        IncomeTotalAmountResponse incomeTotalAmountResponse = IncomeTotalAmountResponse.builder()
                .totalAmount(totalAmount)
                .budgetId(activeBudget.getId())
                .build();

        return CommonMethod.getSuccessResponse(GET_TOTAL_INCOME_AMOUNT, incomeTotalAmountResponse);
    }

    private IncomeDTO convertToDTO(Income income) {
        return IncomeDTO.builder()
                .id(income.getId())
                .userId(income.getUser().getId())
                .amount(income.getAmount())
                .incomeSourceId(income.getIncomeSource() != null ? income.getIncomeSource().getId() : null)
                .incomeSourceName(income.getIncomeSource() != null ? income.getIncomeSource().getName() : null)
                .incomeDate(income.getIncomeDate())
                .build();
    }
}
