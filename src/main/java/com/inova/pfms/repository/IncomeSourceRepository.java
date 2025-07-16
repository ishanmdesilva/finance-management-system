package com.inova.pfms.repository;

import com.inova.pfms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inova.pfms.entity.IncomeSource;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeSourceRepository extends JpaRepository<IncomeSource, String> {
    Optional<IncomeSource> findByUserAndDeletedFalseAndName(User user, String name);
    List<IncomeSource> findByUserAndDeletedFalseOrderByCreatedAtDesc(User user);
}
