package com.inova.pfms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpPendingResponseDTO {

    private String uniqueId;
    private Date generatedOn;
}
