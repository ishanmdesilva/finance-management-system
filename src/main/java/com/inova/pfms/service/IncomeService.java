package com.inova.pfms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inova.pfms.dto.request.AddIncomeDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;

public interface IncomeService {
    /**
     * Adds a new income record.
     *
     * @param dto - income request
     * @return      SuccessResponseDTO
     * @throws JsonProcessingException if JSON processing fails
     */
    SuccessResponseDTO addIncome(AddIncomeDto dto) throws JsonProcessingException;

    /**
     * update an existing income record.
     *
     * @param dto - income request
     * @return      SuccessResponseDTO
     */
    SuccessResponseDTO updateIncome(AddIncomeDto dto, String id);

    /**
     * Retrieves an income record by ID.
     *
     * @param id - income ID
     * @return     SuccessResponseDTO
     */
    SuccessResponseDTO findIncome(String id);

    /**
     * Retrieves all income records.
     *
     *
     * @return     SuccessResponseDTO
     */
    SuccessResponseDTO findAllIncome();

    /**
     * change status of an income record
     *
     * @param id  - income ID
     * @return     SuccessResponseDTO
     */
    SuccessResponseDTO deleteIncome(String id);

    /**
     * Retrieves the total amount of all incomes.
     *
     * @return - SuccessResponseDTO
     */
    SuccessResponseDTO getTotalIncomesAmount();
}
