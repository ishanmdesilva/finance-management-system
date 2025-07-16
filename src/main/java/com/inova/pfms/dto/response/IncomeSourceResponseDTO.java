package com.inova.pfms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeSourceResponseDTO {
    private String id;
    private String name;
    private String description;
    private String userId;
    private boolean deleted;
    private Timestamp createdAt;

}
