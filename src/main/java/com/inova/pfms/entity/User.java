package com.inova.pfms.entity;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean admin;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "activated_at")
    private Timestamp activatedAt;

    @Column(name = "deactivated_at")
    private Timestamp deactivatedAt;

    @ManyToOne
    @JoinColumn(name = "activated_by", foreignKey = @ForeignKey(name = "fk_activated_by"))
    private User activatedBy;

    @ManyToOne
    @JoinColumn(name = "deactivated_by", foreignKey = @ForeignKey(name = "fk_deactivated_by"))
    private User deactivatedBy;
}
