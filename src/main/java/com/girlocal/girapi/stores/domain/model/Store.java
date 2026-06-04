package com.girlocal.girapi.stores.domain.model;

import com.girlocal.girapi.stores.domain.model.enums.StoreStatus;
import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.model.AggregateRoot;

import java.util.UUID;

public class Store extends AggregateRoot<UUID> {

    private String name;
    private String description;
    private StoreStatus status;
    private java.util.List<String> includedZones;
    private java.util.List<String> excludedZones;

    private Store(
            UUID id,
            String name,
            String description,
            StoreStatus status,
            java.util.List<String> includedZones,
            java.util.List<String> excludedZones
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.includedZones = includedZones != null ? java.util.List.copyOf(includedZones) : java.util.List.of();
        this.excludedZones = excludedZones != null ? java.util.List.copyOf(excludedZones) : java.util.List.of();

        this.validateData();
    }

    public static Store create(
            UUID id,
            String name,
            String description
    ) {
        return new Store(
                id != null ? id : UUID.randomUUID(),
                name,
                description,
                StoreStatus.PENDING_APPROVAL,
                java.util.List.of(),
                java.util.List.of()
        );
    }

    public static Store reconstruct(
            UUID id,
            String name,
            String description,
            StoreStatus status,
            java.util.List<String> includedZones,
            java.util.List<String> excludedZones
    ) {
        return new Store(
                id,
                name,
                description,
                status,
                includedZones,
                excludedZones
        );
    }

    /// --- Getters ---
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StoreStatus getStatus() {
        return status;
    }

    public java.util.List<String> getIncludedZones() {
        return includedZones;
    }

    public java.util.List<String> getExcludedZones() {
        return excludedZones;
    }

    /// --- Business Logic ---
    private void validateData() {
        if (this.id == null) {
            throw new DomainException("ID_CANNOT_BE_NULL", "Store Id is required.");
        }
        if (this.name == null || this.name.isBlank()) {
            throw new DomainException("NAME_CANNOT_BE_EMPTY", "Store name cannot be empty.");
        }
        if (this.status == null) {
            throw new DomainException("STATUS_CANNOT_BE_NULL", "Store status is required.");
        }
        if (this.includedZones == null) {
            throw new DomainException("INCLUDED-ZONES_CANNOT_BE_NULL", "Included zones list is required.");
        }
        if (this.excludedZones == null) {
            throw new DomainException("EXCLUDED-ZONES_CANNOT_BE_NULL", "Excluded zones list is required.");
        }
    }

    public void updateDetails(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new DomainException("NAME_CANNOT_BE_EMPTY", "Store name cannot be empty.");
        }
        this.name = name;
        this.description = description;
    }

    public void updateCoverageZones(java.util.List<String> includedZones, java.util.List<String> excludedZones) {
        this.includedZones = includedZones != null ? java.util.List.copyOf(includedZones) : java.util.List.of();
        this.excludedZones = excludedZones != null ? java.util.List.copyOf(excludedZones) : java.util.List.of();
        this.validateData();
    }

    public void approve() {
        if (this.status == StoreStatus.DELETED) {
            throw new DomainException("CANNOT_APPROVE_DELETED_STORE", "Deleted stores cannot be approved.");
        }
        this.status = StoreStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == StoreStatus.DELETED) {
            throw new DomainException("CANNOT_DEACTIVATE_DELETED_STORE", "Deleted stores cannot be deactivated.");
        }
        this.status = StoreStatus.INACTIVE;
    }

    public void suspend() {
        if (this.status == StoreStatus.DELETED) {
            throw new DomainException("CANNOT_SUSPEND_DELETED_STORE", "Deleted stores cannot be suspended.");
        }
        this.status = StoreStatus.SUSPENDED;
    }

    public void delete() {
        this.status = StoreStatus.DELETED;
    }
}
