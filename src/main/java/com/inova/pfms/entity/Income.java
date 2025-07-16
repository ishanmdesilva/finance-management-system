package com.inova.pfms.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "income")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Income {
    @Id
    private String id;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "income_source_id", nullable = false, foreignKey = @ForeignKey(name = "fk_income_income_source1"))
    private IncomeSource incomeSource;

    @Column(name = "income_date")
    private Date incomeDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_income_user1"))
    private User user;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}
