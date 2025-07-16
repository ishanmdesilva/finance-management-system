package com.inova.pfms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBudgetDTO {

    @NotBlank(message = "Budget name is required")
    private String name;
    private String description;
    @NotBlank(message = "Budget start date is required")
    private Date startDate;
    @NotBlank(message = "Budget end date is required")
    private Date endDate;
    private List<CreateBudgetCategoryDTO> budgetCategoryList;
}
