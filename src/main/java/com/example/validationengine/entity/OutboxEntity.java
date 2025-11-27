package com.example.validationengine.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "outbox")
public class OutboxEntity {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID outboxId; 

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId; 

    @Column(name = "payload", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;  

    @Column(name = "status", nullable = false, length = 20)
    private String status;   

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @Column(name = "retry_count")
    private Integer retryCount;
}


