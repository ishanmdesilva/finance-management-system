package com.inova.pfms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExpenseTotalAmountResponse {
    private String budgetId;
    private BigDecimal totalAmount;
}
