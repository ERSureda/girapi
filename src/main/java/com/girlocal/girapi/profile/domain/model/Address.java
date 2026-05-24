package com.girlocal.girapi.profile.domain.model;

import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.model.BaseEntity;

import java.util.UUID;

public class Address extends BaseEntity<UUID> {

    private final UUID userId;
    private final String addressLine1;
    private final String addressLine2;
    private final String locality;
    private final String administrativeArea;
    private final String postalCode;
    private final String countryCode;

    private Address(
            UUID id,
            UUID userId,
            String addressLine1,
            String addressLine2,
            String locality,
            String administrativeArea,
            String postalCode,
            String countryCode
    ) {
        this.id = id;
        this.userId = userId;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.locality = locality;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
        this.countryCode = countryCode;

        this.validateData();
    }

    public static Address create(
            UUID id,
            UUID userId,
            String addressLine1,
            String addressLine2,
            String locality,
            String administrativeArea,
            String postalCode,
            String countryCode
    ) {
        return new Address(
                id != null ? id : UUID.randomUUID(),
                userId,
                addressLine1,
                addressLine2,
                locality,
                administrativeArea,
                postalCode,
                countryCode
        );
    }

    public static Address reconstruct(
            UUID id,
            UUID userId,
            String addressLine1,
            String addressLine2,
            String locality,
            String administrativeArea,
            String postalCode,
            String countryCode
    ) {
        return new Address(
                id,
                userId,
                addressLine1,
                addressLine2,
                locality,
                administrativeArea,
                postalCode,
                countryCode
        );
    }

    /// --- Getters ---
    public UUID getUserId() {
        return userId;
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
            throw new DomainException("ID_CANNOT_BE_NULL", "Address Id is required.");
        }
        if (this.userId == null) {
            throw new DomainException("USER-ID_CANNOT_BE_NULL", "Address UserId is required.");
        }
        if (this.addressLine1 == null || this.addressLine1.isBlank()) {
            throw new DomainException("ADDRESS-LINE1_CANNOT_BE_EMPTY", "Address addressLine1 cannot be empty.");
        }
        if (this.locality == null || this.locality.isBlank()) {
            throw new DomainException("LOCALITY_CANNOT_BE_EMPTY", "Address locality cannot be empty.");
        }
        if (this.postalCode == null || this.postalCode.isBlank()) {
            throw new DomainException("POSTAL-CODE_CANNOT_BE_EMPTY", "Address postalCode cannot be empty.");
        }
        if (this.countryCode == null || this.countryCode.length() != 2) {
            throw new DomainException("COUNTRY-CODE_INVALID", "Address countryCode must be valid.");
        }
    }
}
