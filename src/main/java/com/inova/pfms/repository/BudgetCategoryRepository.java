package com.inova.pfms.repository;

import com.inova.pfms.entity.Budget;
import com.inova.pfms.entity.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, String> {
    List<BudgetCategory> findByDeletedFalseAndBudget(Budget budget);
    Optional<BudgetCategory> findByIdAndDeletedFalse(String id);
}
