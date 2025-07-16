package com.inova.pfms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inova.pfms.dto.request.AddIncomeDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.service.IncomeService;
import jakarta.validation.Valid;
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
 * Controller for managing income-related operations.
 */
@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    /**
     * Adds a new income record.
     *
     * @param dto - income request
     * @return      ResponseEntity
     * @throws JsonProcessingException if JSON processing fails
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PostMapping
    public ResponseEntity<Object> addIncome(@Valid @RequestBody AddIncomeDto dto) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.addIncome(dto));
    }

    /**
     * update an existing income record.
     *
     * @param dto - income request
     * @return      ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateIncome(@Valid @RequestBody AddIncomeDto dto, @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.updateIncome(dto, id));
    }


    /**
     * Retrieves an income record by ID.
     *
     * @param id - income ID
     * @return     ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findIncome(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.findIncome(id));
    }

    /**
     * Retrieves all income records.
     *
     *
     * @return     ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping()
    public ResponseEntity<Object> findAllIncome() {
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.findAllIncome());
    }

    /**
     * change status of an income record
     *
     * @param id  - income ID
     * @return     ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PutMapping("/{id}/deleted")
    public ResponseEntity<Object> deleteIncome(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.deleteIncome(id));
    }


    /**
     * Retrieves the total amount of all incomes.
     *
     * @return - ResponseEntity
     */
    @GetMapping("/total")
    public ResponseEntity<SuccessResponseDTO> totalIncomesAmount() {
        SuccessResponseDTO successResponseDTO = incomeService.getTotalIncomesAmount();
        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

}
