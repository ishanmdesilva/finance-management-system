package com.inova.pfms.controller;

import com.inova.pfms.dto.request.IncomeSourceRequestDTO;
import com.inova.pfms.service.IncomeSourceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing income source-related operations.
 */
@RestController
@RequestMapping("/api/income-sources")
public class IncomeSourceController {

    private final IncomeSourceService incomeSourceService;

    public IncomeSourceController(IncomeSourceService incomeSourceService) {
        this.incomeSourceService = incomeSourceService;
    }

    /**
     * Create a new income source.
     *
     * @param requestDTO -   income source request
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PostMapping
    public ResponseEntity<Object> createIncomeSource(@RequestBody @Valid IncomeSourceRequestDTO requestDTO) {
        return ResponseEntity.ok(incomeSourceService.createIncomeSource(requestDTO));
    }

    /**
     * Update an existing income source.
     *
     * @param id         -   income source ID
     * @param requestDTO -   income source request
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateIncomeSource(@PathVariable String id, @Valid @RequestBody IncomeSourceRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(incomeSourceService.updateIncomeSource(requestDTO, id));
    }

    /**
     * Find an income source by ID.
     *
     * @param id -   income source ID
     * @return    -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findIncomeSource(@PathVariable String id) {
        return ResponseEntity.ok(incomeSourceService.findIncomeSource(id));
    }

    /**
     * Find all income sources for the authenticated user.
     *
     * @return -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @GetMapping()
    public ResponseEntity<Object> findAllIncomeSource() {
        return ResponseEntity.ok(incomeSourceService.findAllIncomeSource());
    }

    /**
     * Delete an income source by ID.
     *
     * @param id -   income source ID
     * @return    -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('REG_USER')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Object> deleteIncomeSource(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(incomeSourceService.deleteIncomeSource(id));
    }

}
