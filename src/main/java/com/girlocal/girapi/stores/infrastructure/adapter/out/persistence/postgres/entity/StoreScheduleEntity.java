package com.girlocal.girapi.stores.infrastructure.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "store_schedules")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StoreScheduleEntity {

    @Id
    private UUID id;

    @Column(name = "store_id")
    private UUID storeId;
}
