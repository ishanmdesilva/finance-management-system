package com.inova.pfms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetResponseDTO {

    private String id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean deleted;
    private Timestamp deletedAt;
    private List<BudgetCategoryResponseDTO> budgetCategories;
}
