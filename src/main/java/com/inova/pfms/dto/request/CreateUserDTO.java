package com.inova.pfms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;  // Make sure to hash the password in service
    private boolean active;
    private boolean admin;
}
