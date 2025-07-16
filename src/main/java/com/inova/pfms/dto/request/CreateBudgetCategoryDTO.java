package com.inova.pfms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBudgetCategoryDTO {
    private String expenseCategoryId;
    private BigDecimal amount;
}
