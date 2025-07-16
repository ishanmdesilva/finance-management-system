package com.inova.pfms.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAlertEvent {

    private String userId;
    private String budgetId;
    private String expenseCategoryId;
    private BigDecimal amount;

}
