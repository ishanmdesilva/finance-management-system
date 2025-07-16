package com.inova.pfms.service.impl;

import com.inova.pfms.dto.request.AddExpenseDto;
import com.inova.pfms.dto.response.ExpenseResponseDto;
import com.inova.pfms.dto.response.ExpenseTotalAmountResponse;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.entity.*;
import com.inova.pfms.exception.ObjectAlreadyExistsException;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.kafka.publisher.BudgetEventPublisher;
import com.inova.pfms.kafka.publisher.ExpenseEventPublisher;
import com.inova.pfms.repository.BudgetCategoryRepository;
import com.inova.pfms.repository.ExpenseRepository;
import com.inova.pfms.repository.UserRepository;
import com.inova.pfms.service.CommonService;
import com.inova.pfms.service.ExpenseService;
import com.inova.pfms.util.CommonMethod;
import com.inova.pfms.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.inova.pfms.constants.LogMessages.*;

@Slf4j
@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseEventPublisher expenseEventPublisher;
    private final UserUtil userUtil;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final CommonService commonService;
    private final BudgetEventPublisher budgetEventPublisher;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, UserUtil userUtil,
                              ExpenseEventPublisher expenseEventPublisher, BudgetCategoryRepository budgetCategoryRepository, CommonService commonService, BudgetEventPublisher budgetEventPublisher) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseEventPublisher = expenseEventPublisher;
        this.userUtil = userUtil;
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.commonService = commonService;
        this.budgetEventPublisher = budgetEventPublisher;
    }

    @Override
    public SuccessResponseDTO getAllExpenses() {
        log.info(GET_ALL_EXPENSES_CALLED);
        List<ExpenseResponseDto> responseAll = new ArrayList<>();
        User loggedInUser = userUtil.getLoggedInUser();
        List<Expense> response = expenseRepository.findByUserAndDeletedFalseOrderByExpenseDateDesc(loggedInUser);
        response.forEach(expense -> responseAll.add(buildExpenseResponseDto(expense)));
        return CommonMethod.getSuccessResponse(GET_ALL_EXPENSE_SUCCESS, responseAll);
    }

    @Override
    public SuccessResponseDTO getExpenseById(String id) {
        log.info(GET_EXPENSE_BY_ID_CALLED, id);
        Optional<Expense> optExpense = expenseRepository.findByIdAndDeletedFalse(id);
        return optExpense.map(expense -> CommonMethod.getSuccessResponse(GET_EXPENSE_SUCCESS, buildExpenseResponseDto(expense))).orElse(null);
    }

    @Override
    public SuccessResponseDTO addExpense(AddExpenseDto dto) {
        log.info(ADD_EXPENSE_CALLED);
        User user = userRepository.findById(userUtil.getLoggedInUser().getId())
                .orElseThrow(() -> new ObjectDoesNotExistException(USER_NOT_FOUND, userUtil.getLoggedInUser().getId()));

        Optional<BudgetCategory> optBudgetCategory = budgetCategoryRepository.findByIdAndDeletedFalse(dto.getBudgetCategoryId());
        if (optBudgetCategory.isEmpty()) {
            throw new ObjectDoesNotExistException(INVALID_BUDGET_CATEGORY_ID, dto.getBudgetCategoryId());
        }
        BudgetCategory budgetCategory = optBudgetCategory.get();

        Expense expense = Expense.builder()
                .id(UUID.randomUUID().toString())
                .description(dto.getDescription())
                .expenseCategory(budgetCategory.getExpenseCategory())
                .budgetCategory(budgetCategory)
                .expenseDate(dto.getExpenseDate())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .amount(dto.getAmount())
                .user(user)
                .deleted(false)
                .build();

        expense = expenseRepository.save(expense);
        log.info(EXPENSE_CREATED_SUCCESS, expense.getId());
        // Publish the expense event to Kafka
        try {
            expenseEventPublisher.publishExpenseCreatedEvent(user.getId(), expense.getId(), budgetCategory.getExpenseCategory().getId(), expense.getAmount(), expense.getExpenseDate());
            log.info(EXPENSE_EVENT_PUBLISHED, expense.getId());
        } catch (Exception e) {
            log.info(EXPENSE_EVENT_PUBLISH_FAILED, expense.getId());
        }

        // update budget categories
        budgetCategory = adjustBudgetCategoryBalance(dto.getAmount(), budgetCategory);

        // Publish the budget alert event to Kafka
        publishBudgetAlertEventToKafka(budgetCategory, user, dto.getAmount());

        return CommonMethod.getSuccessResponse(ADDED_EXPENSE_SUCCESS, buildExpenseResponseDto(expense));
    }

    @Override
    public SuccessResponseDTO updateExpense(String id, AddExpenseDto dto) {
        log.info(UPDATE_EXPENSE_CALLED);
        Optional<Expense> optExpense = expenseRepository.findByIdAndDeletedFalse(id);
        if(optExpense.isEmpty()) {
            throw new ObjectDoesNotExistException(EXPENSE_NOT_FOUND, id);
        }
        Expense expense = optExpense.get();

        if (!expense.getUser().getId().equalsIgnoreCase(userUtil.getLoggedInUser().getId())) {
            throw new ObjectAlreadyExistsException(USER_CANNOT_UPDATE_RECORD);
        }

        Optional<BudgetCategory> optBudgetCategory = budgetCategoryRepository.findByIdAndDeletedFalse(dto.getBudgetCategoryId());
        if (optBudgetCategory.isEmpty()) {
            throw new ObjectDoesNotExistException(INVALID_BUDGET_CATEGORY_ID, dto.getBudgetCategoryId());
        }
        BudgetCategory budgetCategory = optBudgetCategory.get();

        // update budget categories
        if(dto.getAmount().compareTo(expense.getAmount()) != 0) {
            BigDecimal difference = dto.getAmount().subtract(expense.getAmount());
            budgetCategory = adjustBudgetCategoryBalance(difference, budgetCategory);
        }

        expense.setDescription(dto.getDescription());
        expense.setExpenseCategory(budgetCategory.getExpenseCategory());
        expense.setBudgetCategory(budgetCategory);
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        expense.setAmount(dto.getAmount());

        Expense savedExpense = expenseRepository.save(expense);
        log.info(EXPENSE_UPDATED_SUCCESS, expense.getId());

        // Publish the budget alert event to Kafka
        publishBudgetAlertEventToKafka(budgetCategory, expense.getUser(), dto.getAmount());

        return CommonMethod.getSuccessResponse(UPDATED_EXPENSE_SUCCESS, buildExpenseResponseDto(savedExpense));
    }

    @Override
    public boolean deleteExpense(String id) {
        log.info(DELETE_EXPENSE_CALLED, id);
        Optional<Expense> optExpense = expenseRepository.findById(id);
        if (optExpense.isPresent()) {
            Expense expense = optExpense.get();
            expense.setDeleted(true);
            expense.setDeletedAt(new Timestamp(System.currentTimeMillis()));
            expenseRepository.save(expense);

            log.info(EXPENSE_DELETED_SUCCESS, expense.getId());
            return true;
        }
        return false;
    }

    @Override
    public SuccessResponseDTO getTotalExpensesAmount() {
        log.info(GET_EXPENSE_TOTAL_AMOUNT);

        // Get active budget
        Budget activeBudget = commonService.getActiveBudget();
        List<BudgetCategory> budgetCategoryList = budgetCategoryRepository.findByDeletedFalseAndBudget(activeBudget);

        // Calculate total expense amount
        BigDecimal expenseAmount = expenseRepository.findByBudgetCategoryInAndDeletedFalse(budgetCategoryList)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // build response
        ExpenseTotalAmountResponse expenseTotalAmountResponse = ExpenseTotalAmountResponse.builder()
                .totalAmount(expenseAmount)
                .budgetId(activeBudget.getId())
                .build();

        return CommonMethod.getSuccessResponse(GET_TOTAL_EXPENSE_AMOUNT, expenseTotalAmountResponse);
    }

    private ExpenseResponseDto buildExpenseResponseDto(Expense expense) {
        return ExpenseResponseDto.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .expenseCategoryId(Optional.ofNullable(expense.getExpenseCategory()).map(ExpenseCategory::getId).orElse(null))
                .expenseCategoryName(Optional.ofNullable(expense.getExpenseCategory()).map(ExpenseCategory::getName).orElse(null))
                .expenseDate(expense.getExpenseDate())
                .amount(expense.getAmount())
                .userId(expense.getUser().getId())
                .budgetCategoryId(Optional.ofNullable(expense.getBudgetCategory()).map(BudgetCategory::getId).orElse(null))
                .budgetCategoryBalance(Optional.ofNullable(expense.getBudgetCategory()).map(BudgetCategory::getBalance).orElse(null))
                .budgetId(Optional.ofNullable(expense.getBudgetCategory()).map(bc -> bc.getBudget().getId()).orElse(null))
                .budgetName(Optional.ofNullable(expense.getBudgetCategory()).map(bc -> bc.getBudget().getName()).orElse(null))
                .build();
    }

    private BudgetCategory adjustBudgetCategoryBalance(BigDecimal amount, BudgetCategory budgetCategory) {
        log.info(UPDATE_BUDGET_CATEGORY_FOR_EXPENSE, budgetCategory.getId());
        budgetCategory.setBalance(budgetCategory.getBalance().subtract(amount));
        budgetCategory.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return budgetCategoryRepository.save(budgetCategory);

    }

    private void publishBudgetAlertEventToKafka(BudgetCategory budgetCategory, User user, BigDecimal amount) {
        if (budgetCategory.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            try {
                budgetEventPublisher.publishBudgetAlertEvent(user.getId(), budgetCategory.getBudget().getId(), budgetCategory.getExpenseCategory().getId(), amount);
                log.info(BUDGET_EVENT_PUBLISHED, budgetCategory.getBudget().getId());
            } catch (Exception e) {
                log.info(BUDGET_EVENT_PUBLISH_FAILED, budgetCategory.getBudget().getId());
            }
        }
    }

}
