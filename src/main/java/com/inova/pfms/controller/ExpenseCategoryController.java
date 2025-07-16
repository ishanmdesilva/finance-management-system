package com.inova.pfms.controller;

import com.inova.pfms.dto.request.AddExpenseCategoryDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.service.ExpenseCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing expense-category related operations.
 */
@RestController
@RequestMapping("/api/expense-categories")
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    /**
     * Create a new expense category.
     *
     * @param dto -   expense category creation request
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PostMapping
    public ResponseEntity<SuccessResponseDTO> addExpenseCategory(@Valid @RequestBody AddExpenseCategoryDto dto) {
        SuccessResponseDTO successResponseDTO = expenseCategoryService.addExpenseCategory(dto);
        return new ResponseEntity<>(successResponseDTO, HttpStatus.CREATED);
    }

    /**
     * Get all new expense categories.
     *
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping
    public ResponseEntity<SuccessResponseDTO> getAllExpenseCategories() {
        SuccessResponseDTO successResponseDTO = expenseCategoryService.getAllExpenseCategories();
        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    /**
     * Get  expense category by ID.
     *
     * @param id -   expense category ID
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponseDTO> getExpenseCategoryById(@PathVariable String id) {
        SuccessResponseDTO successResponseDTO = expenseCategoryService.getExpenseCategoryById(id);
        if (successResponseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    /**
     * Update expense category.
     *
     * @param id -   expense category ID
     * @param dto -   expense category update request
     * @return  -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponseDTO> updateCategoryExpense(@PathVariable String id, @Valid @RequestBody AddExpenseCategoryDto dto) {
        SuccessResponseDTO successResponseDTO = expenseCategoryService.updateExpenseCategory(id, dto);
        return new ResponseEntity<>(successResponseDTO, HttpStatus.CREATED);
    }

    /**
     * Delete expense category.
     *
     * @param id -   expense category ID
     * @return  -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryExpense(@PathVariable String id) {
        boolean response = expenseCategoryService.deleteExpenseCategory(id);
        if (response) {
            return new ResponseEntity<>("Record successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid expense id", HttpStatus.NOT_FOUND);

        }
    }

    /**
     * Get all expense categories by login user.
     *
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping("/login-user")
    public ResponseEntity<SuccessResponseDTO> getExpenseCategoriesByLoginUser() {
        SuccessResponseDTO successResponseDTO = expenseCategoryService.getExpenseCategoriesByLoginUser();
        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

}
