package com.inova.pfms.constants;

public class LogMessages {
    public static final String USER_CANNOT_UPDATE_RECORD = "User is not allowed to update the record";

    public static final String CREATE_USER_CALLED = "createUser called for email {}";
    public static final String OTP_PUBLISHER_CALLED = "Calling OTPEventPublisher for user {}";
    public static final String OTP_PUBLISHER_COMPLETED = "Completed OTPEventPublisher for user {}";
    public static final String OTP_SENT_SUCCESSFULLY = "OTP sent successfully for email {}";
    public static final String VALIDATE_OTP_CREATE_USER_CALLED = "validateOtpAndCreateUser called for email {}";
    public static final String USER_CREATED_SUCCESSFULLY = "New user created successfully for email {}";
    public static final String UPDATE_USER_CALLED = "updateUser called for email {} and userId {}";
    public static final String UPDATE_USER_SUCCESS = "Update user completed successfully for email {} and userId {}";
    public static final String GET_USER_DETAILS_CALLED = "getUserDetails called";
    public static final String GET_USER_DETAILS_SUCCESS = "getUserDetails completed successfully for email {}";
    public static final String CHANGE_USER_STATUS_CALLED = "changeUserStatus called for userId {} and active: {}";
    public static final String CHANGE_USER_STATUS_SUCCESS = "changeUserStatus completed successfully for userId {} and active: {}";
    public static final String VALIDATE_OTP_CALLED = "validateOtp called for otpId {} and email: {}";
    public static final String CREATE_INCOME_SOURCE_CALLED = "createIncomeSource called";
    public static final String INCOME_SOURCE_ALREADY_EXISTS = "Income source is already added";
    public static final String INCOME_SOURCE_CREATED = "New income source created successfully with id {}";
    public static final String FIND_INCOME_SOURCE_CALLED = "findIncomeSource called for sourceId {}";
    public static final String FIND_INCOME_SOURCE_SUCCESS = "findIncomeSource completed successfully for sourceId {}";
    public static final String INCOME_SOURCE_NOT_FOUND = "Income source not found";
    public static final String FIND_ALL_INCOME_SOURCES_CALLED = "findAllIncomeSource called";
    public static final String FIND_ALL_INCOME_SOURCES_SUCCESS = "findAllIncomeSource completed";
    public static final String UPDATE_INCOME_SOURCE_CALLED = "updateIncomeSource called for sourceId {}";
    public static final String UNAUTHORIZED_UPDATE_ATTEMPT = "User is not allowed to update the income source";
    public static final String INCOME_SOURCE_UPDATED = "Income source updated successfully with id {}";
    public static final String DELETE_INCOME_SOURCE_CALLED = "deleteIncomeSource called for sourceId {}";
    public static final String INCOME_SOURCE_DELETED = "Income source deleted successfully with id {}";
    public static final String ADD_INCOME_CALLED = "addIncome called";
    public static final String INCOME_EVENT_PUBLISHED = "income created event published";
    public static final String INCOME_EVENT_PUBLISH_ERROR = "Error publishing income event: {}";
    public static final String INCOME_ADDED_SUCCESS = "Income added successfully with ID: {}";

    public static final String UPDATE_INCOME_CALLED = "updateIncome called for id {}";
    public static final String INVALID_INCOME_RECORD = "Invalid income record for id {}";
    public static final String INCOME_UPDATED_SUCCESS = "Income updated successfully for ID: {}";

    public static final String FIND_INCOME_CALLED = "findIncome called for id {}";
    public static final String FIND_INCOME_SUCCESS = "findIncome completed for ID: {}";

    public static final String FIND_ALL_INCOME_CALLED = "findAllIncome called";
    public static final String FIND_ALL_INCOME_SUCCESS = "findAllIncome completed";

    public static final String DELETE_INCOME_CALLED = "deleteIncome called for incomeId {}";
    public static final String DELETE_INCOME_SUCCESS = "Income record deleted successfully for incomeId {}";
    public static final String OTP_NOT_FOUND = "OTP request not found";
    public static final String USER_NOT_FOUND = "User not found";
    
    public static final String GET_LOGGED_IN_USER_CALLED = "getLoggedInUser called";
    public static final String GET_LOGGED_IN_USER_COMPLETED = "getLoggedInUser completed";
    public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    
    public static final String NO_PENDING_OTP = "No pending OTP requests";
    
    public static final String GET_ALL_EXPENSES_CALLED = "getAllExpenses called";
    public static final String GET_EXPENSE_BY_ID_CALLED = "getExpenseById called for id {}";
    public static final String ADD_EXPENSE_CALLED = "addExpense called";
    public static final String EXPENSE_CREATED_SUCCESS = "New expense created successfully with id {}";
    public static final String EXPENSE_EVENT_PUBLISHED = "ExpenseCreatedEvent published for expense with id {}";
    public static final String EXPENSE_EVENT_PUBLISH_FAILED = "Failed to publish ExpenseCreatedEvent for expense with id {}";
    public static final String UPDATE_EXPENSE_CALLED = "updateExpense called";
    public static final String EXPENSE_NOT_FOUND = "Expense not found";
    public static final String EXPENSE_UPDATED_SUCCESS = "Expense updated successfully for id {}";
    public static final String DELETE_EXPENSE_CALLED = "deleteExpense called for id {}";
    public static final String EXPENSE_DELETED_SUCCESS = "Expense deleted successfully for id {}";
    public static final String UPDATE_BUDGET_CATEGORY_FOR_EXPENSE = "update budget category id {}";
    public static final String INVALID_BUDGET_CATEGORY_ID = "Invalid budget category ID";
    public static final String GET_EXPENSE_TOTAL_AMOUNT = "Expenses total amount called";
    public static final String GET_INCOME_TOTAL_AMOUNT = "Incomes total amount called";

    public static final String GET_ALL_EXPENSE_CATEGORIES_CALLED = "getAllExpenseCategories called";
    public static final String GET_EXPENSE_CATEGORY_BY_ID_CALLED = "getExpenseCategoryById called for expense categoryId: {}";
    public static final String ADD_EXPENSE_CATEGORY_CALLED = "addExpenseCategory called";
    public static final String EXPENSE_CATEGORY_CREATED_SUCCESS = "New expense category created successfully with id {}";
    public static final String UPDATE_EXPENSE_CATEGORY_CALLED = "updateExpenseCategory called for id {}";
    public static final String EXPENSE_CATEGORY_NOT_FOUND = "Expense category not found";
    public static final String EXPENSE_CATEGORY_UPDATED_SUCCESS = "Expense category updated successfully for id {}";
    public static final String DELETE_EXPENSE_CATEGORY_CALLED = "deleteExpenseCategory called for id {}";
    public static final String EXPENSE_CATEGORY_DELETED_SUCCESS = "Expense category deleted successfully with id {}";
    public static final String GET_ALL_EXPENSE_CATEGORIES_BY_LOGIN_USER = "getExpenseCategoriesByUser called";

    public static final String AUTHENTICATION_CALLED = "user authentication called for email {}";
    public static final String AUTHENTICATION_SUCCESS = "user authentication successful for email {}";
    public static final String FORGET_PASSWORD_CALLED = "forgetPassword called for email {}";
    public static final String OTP_EVENT_PUBLISHER_CALLED = "forgetPassword OtpEventPublisher called for email {}";
    public static final String OTP_EVENT_PUBLISHER_COMPLETED = "forgetPassword OtpEventPublisher completed successfully for email {}";
    public static final String PASSWORD_CHANGED_SUCCESS = "Password changed successfully for email {}";
    public static final String INVALID_CHANGE_PASSWORD_OTP = "Invalid change password otp {} for email {}";
    public static final String VALID_OTP = "Valid otp for email {} and otpId {}";
    public static final String INVALID_OTP = "Invalid otp for email {} and otpId {}";
    public static final String EMAIL_NOT_EXISTS = "Email not exists";
    public static final String OTP_REQUEST_NOT_FOUND = "OTP request not found";
    public static final String NO_PENDING_OTP_REQUESTS = "No pending OTP requests";
    public static final String VERIFY_OTP_AND_CHANGE_PASSWORD_CALLED = "verifyOtpAndChangePassword called for email {}";

    public static final String CREATE_BUDGET_CALLED = "createBudget called for budget {}";
    public static final String BUDGET_CREATED_SUCCESS = "budget created successfully with ID: {}";
    public static final String FIND_ACTIVE_BUDGET_CALLED = "findActiveBudget called for userId {}";
    public static final String FIND_ACTIVE_BUDGET_SUCCESS = "findActiveBudget completed successfully for userId {}";
    public static final String FIND_ALL_BUDGET_CALLED = "findAllBudget called for userId {}";
    public static final String FIND_ALL_BUDGET_SUCCESS = "findAllBudget completed successfully for userId {}";
    public static final String FIND_BUDGET_BY_ID_CALLED = "findBudgetById called for budgetId {}";
    public static final String FIND_BUDGET_BY_ID_SUCCESS = "findBudgetById completed successfully for budgetId {}";
    public static final String DELETE_BUDGET_CALLED = "deleteBudget called for budgetId {}";
    public static final String BUDGET_DELETED_SUCCESS = "budget deleted successfully for budgetId {}";
    public static final String UPDATE_BUDGET_CALLED = "updateBudget called for budgetId {}";
    public static final String BUDGET_UPDATED_SUCCESS = "budget updated successfully for budgetId {}";
    public static final String BUDGET_CATEGORIES_UPDATED = "budget categories updated ";
    public static final String BUDGET_UPDATE_COMPLETED = "updateBudget completed successfully for  budgetId: {}";
    public static final String BUDGET_EVENT_PUBLISHED = "BudgetAlertEvent published for budget with id {}";
    public static final String BUDGET_EVENT_PUBLISH_FAILED = "Failed to publish BudgetAlertEvent for budget with id {}";

    public static final String GET_EXPENSE_CATEGORY_SUCCESS = "Expense category retrieve successfully";
    public static final String GET_ALL_EXPENSE_CATEGORY_SUCCESS = "Expense categories retrieve successfully";
    public static final String UPDATED_EXPENSE_CATEGORY_SUCCESS = "Expense category updated successfully";
    public static final String ADDED_EXPENSE_CATEGORY_SUCCESS = "Expense category added successfully";
    public static final String GET_EXPENSE_SUCCESS = "Expense retrieve successfully";
    public static final String GET_ALL_EXPENSE_SUCCESS = "Expenses retrieve successfully";
    public static final String UPDATED_EXPENSE_SUCCESS = "Expense updated successfully";
    public static final String ADDED_EXPENSE_SUCCESS = "Expense added successfully";
    public static final String GET_TOTAL_EXPENSE_AMOUNT = "Total expense amount retrieve successfully";
    public static final String GET_TOTAL_INCOME_AMOUNT = "Total income amount retrieve successfully";

    public static final String FIND_ALL_EVENT_TYPES_CALLED = "findAllEventTypes called";
    public static final String FIND_ALL_EVENT_TYPES_COMPLETED = "findAllEventTypes completed";
    public static final String INCOME_EVENT_ERROR_HANDLER_CALLED = "handleIncomeEventError called";
    public static final String INCOME_EVENT_ERROR_HANDLER_COMPLETED = "handleIncomeEventError completed successfully for incomeId: {}";
    public static final String CONSUMED_DEAD_LETTER_INCOME = "Consumed dead letter income.created.errors: {}";
    public static final String EVENT_SEARCH_CALLED = "search dead letter event called";
    public static final String EVENT_SEARCH_COMPLETED = "search dead letter event completed";
    public static final String CONSUMED_DEAD_LETTER_EXPENSE = "Consumed dead letter expense.created.errors: {}";
    public static final String EXPENSE_EVENT_ERROR_HANDLER_CALLED = "handleExpenseEventError called";
    public static final String EXPENSE_EVENT_ERROR_HANDLER_COMPLETED = "handleExpenseEventError completed successfully for incomeId: {}";
    public static final String SEARCH_DEAD_LETTER_EVENT_WITH_CRITERIA = "Executing search with eventType: {}, startDate: {}, endDate: {}";

    public static final String GET_ALL_REPORT_TYPES_CALLED = "getAllReportTypes called";
    public static final String GET_ALL_EVENT_TYPES_COMPLETED = "getAllReportTypes completed";
}
