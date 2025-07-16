package com.inova.pfms.dto.response;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncomeDTO {
    private String id;
    private String userId;
    private BigDecimal amount;
    private String incomeSourceId;
    private String incomeSourceName;
    private Date incomeDate;
}
