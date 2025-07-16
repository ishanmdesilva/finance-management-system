package com.inova.pfms.repository;

import com.inova.pfms.entity.BudgetCategory;
import com.inova.pfms.entity.Expense;
import com.inova.pfms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {

    List<Expense> findByUserAndDeletedFalseOrderByExpenseDateDesc(User user);
    Optional<Expense> findByIdAndDeletedFalse(String id);
    List<Expense> findByBudgetCategoryInAndDeletedFalse(List<BudgetCategory> budgetCategoryList);

}
