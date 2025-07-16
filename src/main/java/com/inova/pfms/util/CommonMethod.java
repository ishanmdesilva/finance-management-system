package com.inova.pfms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import lombok.Getter;

public final class CommonMethod {

    private CommonMethod() {
        // Prevent instantiation
    }

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new JavaTimeModule());

    public static SuccessResponseDTO getSuccessResponse(String message, Object data) {
        return new SuccessResponseDTO(CommonConstant.SUCCESS_CODE, message, data);
    }

}