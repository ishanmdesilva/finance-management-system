package com.inova.pfms.entity;

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

import java.util.Date;

@Entity
@Table(name = "dead_letter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeadLetters {
    @Id
    private String id;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "failed_message")
    private String failedMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false, foreignKey = @ForeignKey(name = "fk_created_user1"))
    private User createdBy;
}
