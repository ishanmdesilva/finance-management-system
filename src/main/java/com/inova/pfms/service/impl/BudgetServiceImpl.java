package com.inova.pfms.service.impl;

import com.inova.pfms.dto.request.CreateBudgetDTO;
import com.inova.pfms.dto.request.UpdateBudgetCategoryDTO;
import com.inova.pfms.dto.request.UpdateBudgetDTO;
import com.inova.pfms.dto.response.BudgetCategoryResponseDTO;
import com.inova.pfms.dto.response.BudgetResponseDTO;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.entity.Budget;
import com.inova.pfms.entity.BudgetCategory;
import com.inova.pfms.entity.ExpenseCategory;
import com.inova.pfms.entity.User;
import com.inova.pfms.exception.ObjectCreationFailedException;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.exception.ObjectUpdateFailedException;
import com.inova.pfms.repository.BudgetCategoryRepository;
import com.inova.pfms.repository.BudgetRepository;
import com.inova.pfms.repository.ExpenseCategoryRepository;
import com.inova.pfms.service.BudgetService;
import com.inova.pfms.service.CommonService;
import com.inova.pfms.util.CommonMethod;
import com.inova.pfms.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;

import static com.inova.pfms.constants.LogMessages.*;
import static com.inova.pfms.util.CommonConstant.BUDGET_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;

    private final UserUtil userUtil;
    private final CommonService commonService;


    @Override
    @Transactional
    public SuccessResponseDTO createBudget(CreateBudgetDTO requestDTO) {
        try {
            log.info(CREATE_BUDGET_CALLED, requestDTO.getName());
            User loggedInUser = userUtil.getLoggedInUser();
            List<Budget> existingBudgets = budgetRepository.findOverlappingBudgets(requestDTO.getStartDate(), requestDTO.getEndDate(), loggedInUser.getId());
            if (!existingBudgets.isEmpty()) {
                throw new IllegalArgumentException("A budget already exists for the given time period - date range " + existingBudgets.get(0).getStartDate() + " to " + existingBudgets.get(0).getEndDate());
            }

            Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());

            /*---------- Budget [CREATE] ----------*/
            Budget budget = Budget.builder()
                    .id(UUID.randomUUID().toString())
                    .user(loggedInUser)
                    .name(requestDTO.getName())
                    .description(requestDTO.getDescription())
                    .startDate(requestDTO.getStartDate())
                    .endDate(requestDTO.getEndDate())
                    .createdAt(curTimestamp)
                    .updatedAt(curTimestamp)
                    .deleted(false)
                    .build();
            budget = budgetRepository.save(budget);

            /*---------- Budget Category [CREATE] ----------*/
            List<BudgetCategoryResponseDTO> budgetCategoryList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(requestDTO.getBudgetCategoryList())) {
                Budget savedBudget = budget;

                List<String> expenseCategoryIds = new ArrayList<>();
                requestDTO.getBudgetCategoryList().forEach(category ->
                        expenseCategoryIds.add(category.getExpenseCategoryId()));


                Map<String, ExpenseCategory> expenseCategoryMap = new HashMap<>();
                for (ExpenseCategory category : expenseCategoryRepository.findAllById(expenseCategoryIds)) {
                    expenseCategoryMap.put(category.getId(), category);
                }

                requestDTO.getBudgetCategoryList().forEach(category -> {
                    ExpenseCategory expenseCategory = expenseCategoryMap.get(category.getExpenseCategoryId());
                    if (expenseCategory == null) {
                        throw new IllegalArgumentException("Invalid expense category ID: " + category.getExpenseCategoryId());
                    }
                    BudgetCategory budgetCategory = BudgetCategory.builder()
                            .id(UUID.randomUUID().toString())
                            .budget(savedBudget)
                            .expenseCategory(expenseCategory)
                            .amount(category.getAmount())
                            .balance(category.getAmount())
                            .createdAt(curTimestamp)
                            .updatedAt(curTimestamp)
                            .deleted(false)
                            .build();
                    budgetCategoryRepository.save(budgetCategory);
                    budgetCategoryList.add(convertToBudgetCategoryResponseDTO(budgetCategory));
                });
            }

            BudgetResponseDTO budgetResponseDTO = convertToBudgetResponseDTO(budget);
            budgetResponseDTO.setBudgetCategories(budgetCategoryList);

            log.info(BUDGET_CREATED_SUCCESS, budget.getId());
            return CommonMethod.getSuccessResponse(
                    "Budget created successfully",
                    budgetResponseDTO
            );

        } catch (Exception e) {
            throw new ObjectCreationFailedException("Failed to create budget " + e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO findActiveBudget() {
        try {
            User loggedInUser = userUtil.getLoggedInUser();
            log.info(FIND_ACTIVE_BUDGET_CALLED, loggedInUser.getId());
            Budget activeBudget = commonService.getActiveBudget();
            BudgetResponseDTO budgetResponseDTO = convertToBudgetResponseDTO(activeBudget);
            List<BudgetCategory> budgetCategories = budgetCategoryRepository.findByDeletedFalseAndBudget(activeBudget);
            List<BudgetCategoryResponseDTO> budgetCategoryResponseDTOList = new ArrayList<>();

            if (!CollectionUtils.isEmpty(budgetCategories)) {
                budgetCategories.forEach(category -> {
                    BudgetCategoryResponseDTO budgetCategoryResponseDTO = convertToBudgetCategoryResponseDTO(category);
                    budgetCategoryResponseDTOList.add(budgetCategoryResponseDTO);
                });
            }
            budgetResponseDTO.setBudgetCategories(budgetCategoryResponseDTOList);

            log.info(FIND_ACTIVE_BUDGET_SUCCESS, loggedInUser.getId());
            return CommonMethod.getSuccessResponse(
                    "Active budget retrieved successfully",
                    budgetResponseDTO
            );
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to find active budget ", e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO findAllBudget() {
        try {
            User loggedInUser = userUtil.getLoggedInUser();
            log.info(FIND_ALL_BUDGET_CALLED, loggedInUser.getId());
            List<Budget> budgetList = budgetRepository.findByDeletedFalseAndUserOrderByStartDateDesc(loggedInUser);
            List<BudgetResponseDTO> budgetResponseDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(budgetList)) {
                budgetList.forEach(budget -> {
                    BudgetResponseDTO budgetResponseDTO = convertToBudgetResponseDTO(budget);
                    budgetResponseDTOList.add(budgetResponseDTO);
                });
            }

            log.info(FIND_ALL_BUDGET_SUCCESS, loggedInUser.getId());
            return CommonMethod.getSuccessResponse(
                    "All budgets retrieved successfully",
                    budgetResponseDTOList
            );
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to find all budgets ", e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO findBudgetById(String id) {
        try {
            log.info(FIND_BUDGET_BY_ID_CALLED, id);


            Budget budget = budgetRepository.findById(id)
                    .orElseThrow(() -> new ObjectDoesNotExistException(BUDGET_NOT_FOUND, id));
            BudgetResponseDTO budgetResponseDTO = convertToBudgetResponseDTO(budget);
            List<BudgetCategory> budgetCategories = budgetCategoryRepository.findByDeletedFalseAndBudget(budget);
            List<BudgetCategoryResponseDTO> budgetCategoryResponseDTOList = new ArrayList<>();

            if (!CollectionUtils.isEmpty(budgetCategories)) {
                budgetCategories.forEach(category -> {
                    BudgetCategoryResponseDTO budgetCategoryResponseDTO = convertToBudgetCategoryResponseDTO(category);
                    budgetCategoryResponseDTOList.add(budgetCategoryResponseDTO);
                });
            }
            budgetResponseDTO.setBudgetCategories(budgetCategoryResponseDTOList);

            log.info(FIND_BUDGET_BY_ID_SUCCESS, id);
            return CommonMethod.getSuccessResponse(
                    "Budget retrieved successfully",
                    budgetResponseDTO
            );
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to find budget by ID ", e.getMessage());
        }
    }

    @Override
    public SuccessResponseDTO deleteBudget(String id) {
        try {
            log.info(DELETE_BUDGET_CALLED, id);


            Budget budget = budgetRepository.findById(id)
                    .orElseThrow(() -> new ObjectDoesNotExistException(BUDGET_NOT_FOUND, id));
            budget.setDeleted(true);
            budget.setDeletedAt(new Timestamp(System.currentTimeMillis()));
            budget = budgetRepository.save(budget);

            log.info(BUDGET_DELETED_SUCCESS, id);
            return CommonMethod.getSuccessResponse(
                    "Budget deleted successfully",
                    convertToBudgetResponseDTO(budget)
            );
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to delete budget ", e.getMessage());
        }
    }

    @Override
    @Transactional
    public SuccessResponseDTO updateBudget(UpdateBudgetDTO requestDTO, String id) {
        try {
            log.info(UPDATE_BUDGET_CALLED, id);
            Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
            Budget budget = budgetRepository.findById(id)
                    .orElseThrow(() -> new ObjectDoesNotExistException(BUDGET_NOT_FOUND, id));
            /*---------- Budget [UPDATE] ----------*/
            budget.setName(requestDTO.getName());
            budget.setDescription(requestDTO.getDescription());
            budget.setUpdatedAt(curTimestamp);
            budget = budgetRepository.save(budget);
            log.info(BUDGET_UPDATED_SUCCESS, budget.getId());

            /*---------- Budget Category [UPDATE] ----------*/
            List<BudgetCategoryResponseDTO> budgetCategoryList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(requestDTO.getBudgetCategoryList())) {
                budgetCategoryList = processBudgetCategories(requestDTO, budget);
            }
            log.info(BUDGET_CATEGORIES_UPDATED);

            BudgetResponseDTO responseDTO = convertToBudgetResponseDTO(budget);
            responseDTO.setBudgetCategories(budgetCategoryList);
            log.info(BUDGET_UPDATE_COMPLETED, budget.getId());

            return CommonMethod.getSuccessResponse(
                    "Budget updated successfully",
                    responseDTO
            );
        } catch (ObjectDoesNotExistException e) {
            throw new ObjectDoesNotExistException("Failed to find object by ID ", e.getMessage());
        } catch (Exception e) {
            throw new ObjectUpdateFailedException("Failed to update budget " + e.getMessage());
        }


    }


    private BudgetCategoryResponseDTO convertToBudgetCategoryResponseDTO(BudgetCategory budgetCategory) {
        return BudgetCategoryResponseDTO.builder()
                .id(budgetCategory.getId())
                .expenseCategoryId(budgetCategory.getExpenseCategory().getId())
                .expenseCategoryName(budgetCategory.getExpenseCategory().getName())
                .amount(budgetCategory.getAmount())
                .balance(budgetCategory.getBalance())
                .createdAt(budgetCategory.getCreatedAt())
                .updatedAt(budgetCategory.getUpdatedAt())
                .deleted(budgetCategory.isDeleted())
                .deletedAt(budgetCategory.getDeletedAt())
                .build();
    }

    private BudgetResponseDTO convertToBudgetResponseDTO(Budget budget) {
        return BudgetResponseDTO.builder()
                .id(budget.getId())
                .name(budget.getName())
                .description(budget.getDescription())
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .deleted(budget.isDeleted())
                .deletedAt(budget.getDeletedAt())
                .build();
    }

    private List<BudgetCategoryResponseDTO> processBudgetCategories(UpdateBudgetDTO requestDTO, Budget budget) {
        if (CollectionUtils.isEmpty(requestDTO.getBudgetCategoryList())) {
            return new ArrayList<>();
        }
        Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
        List<String> expenseCategoryIds = new ArrayList<>();
        requestDTO.getBudgetCategoryList().forEach(category -> expenseCategoryIds.add(category.getExpenseCategoryId()));

        Map<String, ExpenseCategory> expenseCategoryMap = new HashMap<>();
        expenseCategoryRepository.findAllById(expenseCategoryIds).forEach(category ->
                expenseCategoryMap.put(category.getId(), category)
        );

        List<BudgetCategoryResponseDTO> budgetCategoryList = new ArrayList<>();
        for (UpdateBudgetCategoryDTO category : requestDTO.getBudgetCategoryList()) {
            ExpenseCategory expenseCategory = expenseCategoryMap.get(category.getExpenseCategoryId());
            if (expenseCategory == null) {
                throw new IllegalArgumentException("Invalid expense category ID: " + category.getExpenseCategoryId());
            }

            BudgetCategory budgetCategory;
            if (category.getId() == null) {
                budgetCategory = BudgetCategory.builder()
                        .id(UUID.randomUUID().toString())
                        .budget(budget)
                        .expenseCategory(expenseCategory)
                        .amount(category.getAmount())
                        .balance(category.getAmount())
                        .createdAt(curTimestamp)
                        .updatedAt(curTimestamp)
                        .deleted(false)
                        .build();
            } else {
                budgetCategory = budgetCategoryRepository.findById(category.getId())
                        .orElseThrow(() -> new ObjectDoesNotExistException("Budget category is not found", category.getId()));
                if (category.isDeleted()) {
                    budgetCategory.setDeleted(true);
                    budgetCategory.setDeletedAt(curTimestamp);
                } else {
                    budgetCategory.setExpenseCategory(expenseCategory);
                    budgetCategory.setAmount(category.getAmount());
                    budgetCategory.setBalance(category.getBalance());
                }
                budgetCategory.setUpdatedAt(curTimestamp);
            }
            budgetCategoryRepository.save(budgetCategory);
            budgetCategoryList.add(convertToBudgetCategoryResponseDTO(budgetCategory));
        }

        return budgetCategoryList;
    }
}
