package com.inova.pfms.kafka.event;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IncomeEvent {
    private String userId;
    private String incomeId;
    private BigDecimal amount;
    private Date date;

    public IncomeEvent(String userId, String incomeId, BigDecimal amount, Date date) {
        this.userId = userId;
        this.incomeId = incomeId;
        this.amount = amount;
        this.date = date;
    }
}
