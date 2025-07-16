package com.inova.pfms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
public class ExpenseResponseDto {

    private String id;
    private String description;
    private String expenseCategoryId;
    private String expenseCategoryName;
    private Date expenseDate;
    private BigDecimal amount;
    private String userId;
    private String budgetCategoryId;
    private BigDecimal budgetCategoryBalance;
    private String budgetId;
    private String budgetName;

}
