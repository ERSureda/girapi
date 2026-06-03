package com.girlocal.girapi.stores.infrastructure.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StoreEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 30)
    private String status;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "included_zones", columnDefinition = "text[]")
    private List<String> includedZones;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "excluded_zones", columnDefinition = "text[]")
    private List<String> excludedZones;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}