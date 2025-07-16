package com.inova.pfms.repository;

import com.inova.pfms.entity.Budget;
import com.inova.pfms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {

    List<Budget> findByDeletedFalseAndUserOrderByStartDateDesc(User user);
    List<Budget> findByUserIdAndDeletedFalseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String userId, Date inputDate1, Date inputDate2
    );

    @Query("SELECT b FROM Budget b " +
            "WHERE NOT (:startDate > b.endDate OR :endDate < b.startDate) " +
            "AND b.user.id = :userId " +
            "AND b.deleted = false")
    List<Budget> findOverlappingBudgets(@Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate,
                                        @Param("userId") String userId);
}
