package com.inova.pfms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "budget_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetCategory {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false, foreignKey = @ForeignKey(name = "fk_budget_category_budget1"))
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "expense_category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_budget_category_expense_category1"))
    private ExpenseCategory expenseCategory;
    
    @OneToMany(mappedBy = "budgetCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses;

    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}
