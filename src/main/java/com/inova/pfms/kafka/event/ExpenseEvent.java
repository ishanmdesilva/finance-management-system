package com.inova.pfms.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseEvent {
    private String userId;
    private String expenseId;
    private String expenseCategoryId;
    private BigDecimal amount;
    private Date date;
}
