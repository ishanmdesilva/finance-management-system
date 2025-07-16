package com.inova.pfms.service;

import com.inova.pfms.dto.request.CreateBudgetDTO;
import com.inova.pfms.dto.request.UpdateBudgetDTO;
import com.inova.pfms.dto.response.SuccessResponseDTO;

public interface BudgetService {

    /**
     * Create a new budget.
     *
     * @param requestDTO -   budget creation request
     * @return        -   SuccessResponseDTO
     */
    SuccessResponseDTO createBudget(CreateBudgetDTO requestDTO);

    /**
     * Find currently active budget
     *
     * @return        -   SuccessResponseDTO
     */
    SuccessResponseDTO findActiveBudget();

    /**
     * Find all budgets
     *
     * @return        -   SuccessResponseDTO
     */
    SuccessResponseDTO findAllBudget();

    /**
     * Find budget by ID
     *
     * @param id      -   budget id
     * @return        -   SuccessResponseDTO
     */
    SuccessResponseDTO findBudgetById(String id);

    /**
     * change the status of a budget
     *
     * @param id      -   budget id
     * @return        -   SuccessResponseDTO
     */
    SuccessResponseDTO deleteBudget(String id);

    /**
     * Update a budget
     *
     * @param  requestDTO -   budget update request
     * @param id          -   budget id
     * @return            -   SuccessResponseDTO
     */
    SuccessResponseDTO updateBudget(UpdateBudgetDTO requestDTO, String id);
}
