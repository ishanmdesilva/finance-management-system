package com.inova.pfms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeSourceRequestDTO {

    @NotBlank(message = "Income source name is required")
    private String name;
    private String description;
}
