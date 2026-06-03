package com.girlocal.girapi.stores.infrastructure.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "store_details")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StoreDetailsEntity {

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "active_address_id")
    private UUID activeAddressId;
}
