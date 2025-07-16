package com.inova.pfms.controller;

import com.inova.pfms.dto.request.CreateBudgetDTO;
import com.inova.pfms.dto.request.UpdateBudgetDTO;
import com.inova.pfms.service.BudgetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for managing budget-related operations.
 */
@RestController
@RequestMapping("/api/budgets")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class BudgetController {

    private final BudgetService budgetService;

    /**
     * Create a new budget.
     *
     * @param requestDTO -   budget creation request
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PostMapping
    public ResponseEntity<Object> createBudget(@Valid @RequestBody CreateBudgetDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(requestDTO));
    }

    /**
     * Find currently active budget
     *
     *
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping("/active")
    public ResponseEntity<Object> findActiveBudget() {
        return ResponseEntity.status(HttpStatus.OK).body(budgetService.findActiveBudget());
    }

    /**
     * Find all budgets
     *
     * @return        -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping()
    public ResponseEntity<Object> findAllBudget() {
        return ResponseEntity.status(HttpStatus.OK).body(budgetService.findAllBudget());
    }

    /**
     * Find budget by ID
     *
     * @param id      -   budget id
     * @return        -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findBudgetById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(budgetService.findBudgetById(id));
    }

    /**
     * change the status of a budget
     *
     * @param id      -   budget id
     * @return        -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PutMapping("/{id}/delete")
    public ResponseEntity<Object> deleteBudget(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(budgetService.deleteBudget(id));
    }

    /**
     * Update a budget
     *
     * @param  requestDTO -   budget update request
     * @param id          -   budget id
     * @return            -   SuccessResponseDTO
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBudget(@PathVariable String id,@Valid @RequestBody UpdateBudgetDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(budgetService.updateBudget(requestDTO, id));
    }
}
