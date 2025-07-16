package com.inova.pfms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetCategoryResponseDTO {
    private String id;
    private String expenseCategoryId;
    private String expenseCategoryName;
    private BigDecimal amount;
    private BigDecimal balance;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean deleted;
    private Timestamp deletedAt;

}
