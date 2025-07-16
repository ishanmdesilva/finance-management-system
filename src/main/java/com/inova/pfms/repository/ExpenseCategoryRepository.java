package com.inova.pfms.repository;

import com.inova.pfms.entity.ExpenseCategory;
import com.inova.pfms.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseCategoryRepository extends CrudRepository<ExpenseCategory, String> {

    List<ExpenseCategory> findByUserAndDeletedFalseOrderByCreatedAtDesc(User user);
    ExpenseCategory findByIdAndDeletedFalse(String id);
    List<ExpenseCategory> findByUserIdAndDeletedFalse(String userId);

}
