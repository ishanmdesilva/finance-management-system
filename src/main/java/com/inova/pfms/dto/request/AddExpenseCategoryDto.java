package com.inova.pfms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddExpenseCategoryDto {

    @NotBlank(message = "Expense category name is required")
    private String name;
    private String description;

}
