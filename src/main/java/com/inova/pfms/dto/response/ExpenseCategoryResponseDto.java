package com.inova.pfms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseCategoryResponseDto {

    private String id;
    private String name;
    private String description;
    private String userId;

}
