package com.inova.pfms.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "otp_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRequest {
    @Id
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Column(name = "data")
    private String data;

    @Column(name = "status")
    private String status;

    @Column(name = "generated_on", nullable = false)
    private Timestamp generatedOn;

    @Column(name = "attempts")
    private Integer attempts;
}
