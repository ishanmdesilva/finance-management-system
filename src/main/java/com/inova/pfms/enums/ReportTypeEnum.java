package com.inova.pfms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ReportTypeEnum {
    INCOME("INCOME", "income"),
    EXPENSE("EXPENSE", "expense"),
    BUDGET("BUDGET", "budget wise expenses"),;

    private String code;
    private String name;

}
