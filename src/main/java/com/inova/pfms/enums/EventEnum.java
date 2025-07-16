package com.inova.pfms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EventEnum {
    INCOME_EVENT("INCOME", "income.created"),
    EXPENSE_EVENT("EXPENSE", "expense.created");

    private String name;
    private String code;
}
