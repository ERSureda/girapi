package com.girlocal.girapi.shared.infraestructure.adapter.out.outbox;

import com.girlocal.girapi.shared.infraestructure.utils.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
// Indexes on this table are partial indexes managed by Flyway (V1__create_initial_schema.sql):
//   · idx_outbox_pending  → ON (created_at) WHERE processed = false  (relay worker)
//   · idx_outbox_cleanup  → ON (processed_at) WHERE processed = true  (cleanup worker)
// Partial indexes cannot be expressed via @Index — do not add them here.
@Table(name = "outbox_events")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEntity {

    @Id
    private UUID id;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "processed", nullable = false)
    private boolean processed;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    public OutboxEntity(String aggregateType, String aggregateId, String payload) {
        this.id = UuidGenerator.generateId();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.createdAt = Instant.now();
        this.processed = false;
        this.retryCount = 0;
    }

    public void markAsProcessed() {
        this.processed = true;
        this.processedAt = Instant.now();
        this.errorMessage = null;
    }

    public void markAsFailed(String errorMessage) {
        this.errorMessage = errorMessage;
        this.retryCount++;
    }
}
