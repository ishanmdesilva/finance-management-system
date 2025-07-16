package com.inova.pfms.controller;

import com.inova.pfms.dto.request.AddExpenseDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing expense-related operations.
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Create a new expense.
     *
     * @param dto -   expense creation request
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PostMapping
    public ResponseEntity<SuccessResponseDTO> addExpense(@RequestBody @Valid AddExpenseDto dto) {
        SuccessResponseDTO successResponseDTO = expenseService.addExpense(dto);
        return new ResponseEntity<>(successResponseDTO, HttpStatus.CREATED);
    }

    /**
     * Get all expenses.
     *
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping
    public ResponseEntity<SuccessResponseDTO> getAllExpenses() {
        SuccessResponseDTO successResponseDTO = expenseService.getAllExpenses();
        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    /**
     * Get an expense by ID.
     *
     * @param id -   expense ID
     * @return      -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponseDTO> getExpenseById(@PathVariable String id) {
        SuccessResponseDTO successResponseDTO = expenseService.getExpenseById(id);
        if (successResponseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    /**
     * Update an existing expense.
     *
     * @param id  -   expense ID
     * @param dto -   expense update request
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponseDTO> updateExpense(@PathVariable String id, @Valid @RequestBody AddExpenseDto dto) {
        SuccessResponseDTO successResponseDTO = expenseService.updateExpense(id, dto);
        return new ResponseEntity<>(successResponseDTO, HttpStatus.CREATED);
    }

    /**
     * Delete an expense by ID.
     *
     * @param id -   expense ID
     * @return      -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable String id) {
        boolean response = expenseService.deleteExpense(id);
        if (response) {
            return new ResponseEntity<>("Record successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid expense id", HttpStatus.NOT_FOUND);

        }
    }

    /**
     * Get the total amount of expenses.
     *
     * @return           -   ResponseEntity
     */
    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('REG_USER')")
    public ResponseEntity<SuccessResponseDTO> totalExpensesAmount() {
        SuccessResponseDTO successResponseDTO = expenseService.getTotalExpensesAmount();
        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

}
