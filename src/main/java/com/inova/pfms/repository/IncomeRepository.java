package com.inova.pfms.repository;

import com.inova.pfms.entity.IncomeSource;
import com.inova.pfms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.inova.pfms.entity.Income;

import java.sql.Date;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, String> {
    List<Income> findByUserAndDeletedFalseOrderByIncomeDateDesc(User user);

    List<Income> findByDeletedFalseAndUserAndIncomeDateBetween(User loggedInUser, Date startDate, Date endDate);
}
