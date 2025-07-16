package com.inova.pfms.repository;

import com.inova.pfms.entity.DeadLetters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface DeadLettersRepository extends JpaRepository<DeadLetters, String>, DeadLetterCustomRepository {
}
