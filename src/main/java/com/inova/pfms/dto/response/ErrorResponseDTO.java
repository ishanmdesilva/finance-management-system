package com.inova.pfms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String code;
    private String message;
}

