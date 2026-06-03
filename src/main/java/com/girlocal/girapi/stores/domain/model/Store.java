package com.girlocal.girapi.stores.domain.model;

import com.girlocal.girapi.stores.domain.model.enums.StoreStatus;
import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.model.AggregateRoot;

import java.util.UUID;

public class Store extends AggregateRoot<UUID> {

    private String name;
    private String description;
    private UUID activeAddressId;
    private StoreStatus status;

    private Store(
            UUID id,
            String name,
            String description,
            UUID activeAddressId,
            StoreStatus status
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.activeAddressId = activeAddressId;
        this.status = status;

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
                null,
                StoreStatus.PENDING_APPROVAL
        );
    }

    public static Store reconstruct(
            UUID id,
            String name,
            String description,
            UUID activeAddressId,
            StoreStatus status
    ) {
        return new Store(
                id,
                name,
                description,
                activeAddressId,
                status
        );
    }

    /// --- Getters ---
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getActiveAddressId() {
        return activeAddressId;
    }

    public StoreStatus getStatus() {
        return status;
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
    }

    public void updateDetails(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new DomainException("NAME_CANNOT_BE_EMPTY", "Store name cannot be empty.");
        }
        this.name = name;
        this.description = description;
    }

    public void setActiveAddress(UUID addressId) {
        this.activeAddressId = addressId;
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
