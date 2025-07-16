package com.inova.pfms.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;

@Entity
@Table(name = "income_sources", uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_id_name", columnNames = {"name", "user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeSource {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_income_source_user1"))
    private User user;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}
