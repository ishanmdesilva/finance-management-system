package com.inova.pfms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
public class AddExpenseDto {

    private String description;

    @NotBlank(message = "Budget category ID cannot be null or empty")
    private String budgetCategoryId;

    private Date expenseDate;

    @NotBlank(message = "Amount cannot be null or empty")
    private BigDecimal amount;

}
