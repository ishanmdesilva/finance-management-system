package com.inova.pfms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
}
