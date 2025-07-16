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
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    private String id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "expense_category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_expense_category"))
    private ExpenseCategory expenseCategory;
    
    @ManyToOne
    @JoinColumn(name = "budget_category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_budget_category"))
    private BudgetCategory budgetCategory;
    
    @Column(name = "expense_date")
    private Date expenseDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_user1"))
    private User user;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}
