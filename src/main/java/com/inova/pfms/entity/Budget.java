package com.inova.pfms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_budget_user1"))
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetCategory> budgetCategories;
}
