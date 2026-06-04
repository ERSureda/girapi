package com.girlocal.girapi.stores.infrastructure.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "store_members")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StoreMemberEntity {

    @Id
    private Long id;

    private UUID storeId;

    private UUID userId;

    private String role;

    private String status;
}
