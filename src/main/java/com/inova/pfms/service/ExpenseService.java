package com.inova.pfms.service;

import com.inova.pfms.dto.request.AddExpenseDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;

public interface ExpenseService {

     /**
      * Retrieves all expenses.
      *
      * @return a SuccessResponseDTO containing the list of all expenses.
      */
     SuccessResponseDTO getAllExpenses();

     /**
       * Retrieves an expense by its ID.
       *
       * @param id the ID of the expense to retrieve.
       * @return a SuccessResponseDTO containing the expense details.
       */
     SuccessResponseDTO getExpenseById(String id);

     /**
      * Add new expenses.
      *
      * @param dto expense details.
      * @return a SuccessResponseDTO containing the expense details.
      */
     SuccessResponseDTO addExpense(AddExpenseDto dto);

     /**
       * Updates an existing expense.
       *
       * @param id the ID of the expense to update.
       * @param dto the updated expense details.
       * @return a SuccessResponseDTO containing the updated expense details.
       */
     SuccessResponseDTO updateExpense(String id, AddExpenseDto dto);

     /**
       * Deletes an expense by its ID.
       *
       * @param id the ID of the expense to delete.
       * @return a boolean indicating whether the deletion was successful.
       */
     boolean deleteExpense(String id);

     /**
       * Retrieves total expenses amount.
       *
       *
       * @return a SuccessResponseDTO.
       */
     SuccessResponseDTO getTotalExpensesAmount();
}
