package com.girlocal.girapi.stores.domain.model;

import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.model.BaseEntity;

import java.util.UUID;

public class StoreAddress extends BaseEntity<UUID> {

    private final UUID storeId;
    private final String addressLine1;
    private final String addressLine2;
    private final String locality;
    private final String administrativeArea;
    private final String postalCode;
    private final String countryCode;

    private StoreAddress(
            UUID id,
            UUID storeId,
            String addressLine1,
            String addressLine2,
            String locality,
            String administrativeArea,
            String postalCode,
            String countryCode
    ) {
        this.id = id;
        this.storeId = storeId;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.locality = locality;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
        this.countryCode = countryCode;

        this.validateData();
    }

    public static StoreAddress create(
            UUID id,
            UUID storeId,
            String addressLine1,
            String addressLine2,
            String locality,
            String administrativeArea,
            String postalCode,
            String countryCode
    ) {
        return new StoreAddress(
                id != null ? id : UUID.randomUUID(),
                storeId,
                addressLine1,
                addressLine2,
                locality,
                administrativeArea,
                postalCode,
                countryCode
        );
    }

    public static StoreAddress reconstruct(
            UUID id,
            UUID storeId,
            String addressLine1,
            String addressLine2,
            String locality,
            String administrativeArea,
            String postalCode,
            String countryCode
    ) {
        return new StoreAddress(
                id,
                storeId,
                addressLine1,
                addressLine2,
                locality,
                administrativeArea,
                postalCode,
                countryCode
        );
    }

    /// --- Getters ---
    public UUID getStoreId() {
        return storeId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getLocality() {
        return locality;
    }

    public String getAdministrativeArea() {
        return administrativeArea;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    /// --- Business Logic ---
    private void validateData() {
        if (this.id == null) {
            throw new DomainException("ID_CANNOT_BE_NULL", "StoreAddress Id is required.");
        }
        if (this.storeId == null) {
            throw new DomainException("STORE-ID_CANNOT_BE_NULL", "StoreAddress storeId is required.");
        }
        if (this.addressLine1 == null || this.addressLine1.isBlank()) {
            throw new DomainException("ADDRESS-LINE1_CANNOT_BE_EMPTY", "StoreAddress addressLine1 cannot be empty.");
        }
        if (this.locality == null || this.locality.isBlank()) {
            throw new DomainException("LOCALITY_CANNOT_BE_EMPTY", "StoreAddress locality cannot be empty.");
        }
        if (this.postalCode == null || this.postalCode.isBlank()) {
            throw new DomainException("POSTAL-CODE_CANNOT_BE_EMPTY", "StoreAddress postalCode cannot be empty.");
        }
        if (this.countryCode == null || this.countryCode.length() != 2) {
            throw new DomainException("COUNTRY-CODE_INVALID", "StoreAddress countryCode must be valid.");
        }
    }
}
