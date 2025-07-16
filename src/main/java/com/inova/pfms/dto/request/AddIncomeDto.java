package com.inova.pfms.dto.request;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddIncomeDto {
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;
    @NotBlank(message = "Income source ID cannot be blank")
    private String incomeSourceId;
    private Date incomeDate;
}
