package com.inova.pfms.dto.response;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for User entity.
 * Avoids exposing sensitive fields like password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean active;
    private boolean admin;
    private Timestamp createdAt;
    private Timestamp activatedAt;
    private Timestamp deactivatedAt;
    
    private String activatedById;
    private String deactivatedById;
}
