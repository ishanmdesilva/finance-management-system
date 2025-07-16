package com.inova.pfms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeadLetterEventResponseDTO {
    private String id;
    private String eventType;
    private Date createdAt;
    private String createdUserFname;
    private String createdUserLname;
    private String createdUserEmail;
    private Object data;
}
