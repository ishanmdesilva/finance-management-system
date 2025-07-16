package com.inova.pfms.repository;

import com.inova.pfms.entity.DeadLetters;

import java.util.Date;
import java.util.List;

public interface DeadLetterCustomRepository {
    List<DeadLetters> search(String eventType, Date startDate, Date endDate);
}
