package com.inova.pfms.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Entity
@Table(name = "expense_category", uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_id_category_name", columnNames = {"name", "user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategory {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_category_user1"))
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

}
