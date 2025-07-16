package com.inova.pfms.service.impl;

import static com.inova.pfms.constants.LogMessages.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.util.CommonMethod;
import org.springframework.stereotype.Service;

import com.inova.pfms.dto.request.AddExpenseCategoryDto;
import com.inova.pfms.dto.response.ExpenseCategoryResponseDto;
import com.inova.pfms.entity.ExpenseCategory;
import com.inova.pfms.entity.User;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.repository.ExpenseCategoryRepository;
import com.inova.pfms.repository.UserRepository;
import com.inova.pfms.service.ExpenseCategoryService;
import com.inova.pfms.util.UserUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;
    private final UserUtil userUtil;

    public ExpenseCategoryServiceImpl(ExpenseCategoryRepository expenseCategoryRepository, UserRepository userRepository,
                                      UserUtil userUtil) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.userRepository = userRepository;
        this.userUtil = userUtil;
    }

    @Override
    public SuccessResponseDTO getAllExpenseCategories() {
        log.info(GET_ALL_EXPENSE_CATEGORIES_CALLED);
        List<ExpenseCategoryResponseDto> responseAll = new ArrayList<>();
        List<ExpenseCategory> response = expenseCategoryRepository.findByUserAndDeletedFalseOrderByCreatedAtDesc(userUtil.getLoggedInUser());
        response.forEach(exCategory -> responseAll.add(buildExpenseCategoryResponseDto(exCategory)));

        return CommonMethod.getSuccessResponse(GET_ALL_EXPENSE_CATEGORY_SUCCESS, responseAll);
    }

    @Override
    public SuccessResponseDTO getExpenseCategoryById(String id) {
        log.info(GET_EXPENSE_CATEGORY_BY_ID_CALLED, id);
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByIdAndDeletedFalse(id);
        if (expenseCategory == null) {
            return null;
        }
        return CommonMethod.getSuccessResponse(GET_EXPENSE_CATEGORY_SUCCESS,
                buildExpenseCategoryResponseDto(expenseCategory));
    }

    @Override
    public SuccessResponseDTO addExpenseCategory(AddExpenseCategoryDto dto) {
        log.info(ADD_EXPENSE_CATEGORY_CALLED);

        User user = userRepository.findById(userUtil.getLoggedInUser().getId())
                .orElseThrow(() -> new ObjectDoesNotExistException(USER_NOT_FOUND, userUtil.getLoggedInUser().getId()));

        ExpenseCategory expenseCategory = ExpenseCategory.builder()
                .id(UUID.randomUUID().toString())
                .name(dto.getName())
                .description(dto.getDescription())
                .user(user)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .deleted(false)
                .build();

        expenseCategory = expenseCategoryRepository.save(expenseCategory);
        log.info(EXPENSE_CATEGORY_CREATED_SUCCESS, expenseCategory.getId());
        return CommonMethod.getSuccessResponse(ADDED_EXPENSE_CATEGORY_SUCCESS,
                buildExpenseCategoryResponseDto(expenseCategory));
    }

    @Override
    public SuccessResponseDTO updateExpenseCategory(String id, AddExpenseCategoryDto dto) {
        log.info(UPDATE_EXPENSE_CATEGORY_CALLED, id);
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByIdAndDeletedFalse(id);
        if(expenseCategory == null) {
            throw new ObjectDoesNotExistException(EXPENSE_CATEGORY_NOT_FOUND, id);
        }

        User user = userRepository.findById(userUtil.getLoggedInUser().getId())
                .orElseThrow(() -> new ObjectDoesNotExistException(USER_NOT_FOUND, userUtil.getLoggedInUser().getId()));

        expenseCategory.setName(dto.getName());
        expenseCategory.setDescription(dto.getDescription());
        expenseCategory.setUser(user);
        expenseCategory.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        expenseCategory = expenseCategoryRepository.save(expenseCategory);
        log.info(EXPENSE_CATEGORY_UPDATED_SUCCESS, expenseCategory.getId());
        return CommonMethod.getSuccessResponse(UPDATED_EXPENSE_CATEGORY_SUCCESS,
                buildExpenseCategoryResponseDto(expenseCategory));
    }

    @Override
    public boolean deleteExpenseCategory(String id) {
        log.info(DELETE_EXPENSE_CATEGORY_CALLED, id);
        Optional<ExpenseCategory> optExpenseCategory = expenseCategoryRepository.findById(id);
        if (optExpenseCategory.isPresent()) {
            ExpenseCategory expenseCategory = optExpenseCategory.get();
            expenseCategory.setDeleted(true);
            expenseCategory.setDeletedAt(new Timestamp(System.currentTimeMillis()));
            expenseCategoryRepository.save(expenseCategory);
            log.info(EXPENSE_CATEGORY_DELETED_SUCCESS, id);
            return true;
        }
        return false;
    }

    @Override
    public SuccessResponseDTO getExpenseCategoriesByLoginUser() {
        log.info(GET_ALL_EXPENSE_CATEGORIES_BY_LOGIN_USER);
        List<ExpenseCategoryResponseDto> responseAll = new ArrayList<>();
        List<ExpenseCategory> response = expenseCategoryRepository.findByUserIdAndDeletedFalse(userUtil.getLoggedInUser().getId());
        response.forEach(exCategory -> responseAll.add(buildExpenseCategoryResponseDto(exCategory)));

        return CommonMethod.getSuccessResponse(GET_ALL_EXPENSE_CATEGORY_SUCCESS, responseAll);
    }

    private ExpenseCategoryResponseDto buildExpenseCategoryResponseDto(ExpenseCategory expenseCategory) {
        return ExpenseCategoryResponseDto.builder()
                .id(expenseCategory.getId())
                .name(expenseCategory.getName())
                .description(expenseCategory.getDescription())
                .userId(expenseCategory.getUser().getId())
                .build();
    }
}
