package com.inova.pfms.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstant {

    /*------- Response Code --------------------*/
    public static final String SUCCESS_CODE = "Success";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    /*-----------ERROR MESSAGES ---------------*/
    public static final String BUDGET_NOT_FOUND = "Budget not found";
    public static final String INCOME_RECORD_NOT_FOUND = "Income record is not found";
}

