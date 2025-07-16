package com.inova.pfms.service;

import com.inova.pfms.dto.request.IncomeSourceRequestDTO;
import com.inova.pfms.dto.response.SuccessResponseDTO;

public interface IncomeSourceService {

    /**
     * Creates a new income source.
     *
     * @param requestDTO the income source request DTO
     * @return the success response DTO
     */
    SuccessResponseDTO createIncomeSource(IncomeSourceRequestDTO requestDTO);

    /**
     * Finds an income source by its ID.
     *
     * @param sourceId the income source ID
     * @return the success response DTO
     */
    SuccessResponseDTO findIncomeSource(String sourceId);

    /**
     * Finds all income sources.
     *
     * @return the success response DTO
     */
    SuccessResponseDTO findAllIncomeSource();

    /**
     * Updates an existing income source.
     *
     * @param requestDTO the income source request DTO
     * @param sourceId   the income source ID
     * @return the success response DTO
     */
    SuccessResponseDTO updateIncomeSource(IncomeSourceRequestDTO requestDTO, String sourceId);

    /**
     * Deletes an income source by its ID.
     *
     * @param sourceId the income source ID
     * @return the success response DTO
     */
    SuccessResponseDTO deleteIncomeSource(String sourceId);
}
