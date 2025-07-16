package com.inova.pfms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponseDTO {
    private String code;
    private String message;
    private Object data;
}
