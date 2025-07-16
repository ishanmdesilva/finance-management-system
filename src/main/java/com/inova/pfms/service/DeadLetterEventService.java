package com.inova.pfms.service;

import com.inova.pfms.dto.response.SuccessResponseDTO;

import java.util.Map;

public interface DeadLetterEventService {

    /**
     * Retrieves all event types
     *
     * @return  -  SuccessResponseDTO
     */
    SuccessResponseDTO findAllEventTypes();

    /**
     * Handle income error consumer event
     *
     * @param message - error consumer message
     */
    void handleIncomeEventError(String message);

    /**
     * Handle expense error consumer event
     *
     * @param message - error expense message
     */
    void handleExpenseEventError(String message);

    /**
     * search dead letter events based on the provided criteria
     *
     * @return  -  SuccessResponseDTO
     */
    SuccessResponseDTO searchEvent(Map<String, Object> searchParams);
}
