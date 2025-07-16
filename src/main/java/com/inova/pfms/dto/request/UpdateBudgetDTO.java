package com.inova.pfms.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBudgetDTO {
    @NotBlank(message = "name is required")
    private String name;
    private String description;
    private List<UpdateBudgetCategoryDTO> budgetCategoryList;
}

