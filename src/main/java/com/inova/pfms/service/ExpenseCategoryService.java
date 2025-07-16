package com.inova.pfms.service;

import com.inova.pfms.dto.request.AddExpenseCategoryDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;

public interface ExpenseCategoryService {

    /**
     * Get all new expense categories.
     *
     * @return           -   ResponseEntity
     */
    SuccessResponseDTO getAllExpenseCategories();

    /**
     * Get  expense category by ID.
     *
     * @param id -   expense category ID
     * @return           -   ResponseEntity
     */
    SuccessResponseDTO getExpenseCategoryById(String id);

    /**
     * Create a new expense category.
     *
     * @param dto -   expense category creation request
     * @return           -   SuccessResponseDTO
     */
    SuccessResponseDTO addExpenseCategory(AddExpenseCategoryDto dto);

    /**
     * Update an existing expense category.
     *
     * @param id -   expense category ID
     * @param dto -   expense category update request
     * @return           -   SuccessResponseDTO
     */
    SuccessResponseDTO updateExpenseCategory(String id, AddExpenseCategoryDto dto);

    /**
     * Delete an expense category.
     *
     * @param id -   expense category ID
     * @return           -   boolean
     */
    boolean deleteExpenseCategory(String id);

    /**
     * Get all expense categories by login user.
     *
     * @return           -   SuccessResponseDTO
     */
    SuccessResponseDTO getExpenseCategoriesByLoginUser();

}
