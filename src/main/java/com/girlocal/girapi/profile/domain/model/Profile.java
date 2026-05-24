package com.girlocal.girapi.profile.domain.model;

import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.model.BaseEntity;

import java.util.UUID;

public class Profile extends BaseEntity<UUID> {

    private String firstName;
    private String lastName;
    private String phone;
    private UUID activeAddressId;

    /// --- Constructors ---
    private Profile(
            UUID userId,
            String firstName,
            String lastName,
            String phone,
            UUID activeAddressId
    ) {
        this.id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.activeAddressId = activeAddressId;

        this.validateData();
    }

    public static Profile create(
            UUID userId,
            String firstName,
            String lastName,
            String phone
    ) {
        return new Profile(
                userId,
                firstName,
                lastName,
                phone,
                null
        );
    }

    public static Profile reconstruct(
            UUID userId,
            String firstName,
            String lastName,
            String phone,
            UUID activeAddressId
    ) {
        return new Profile(
                userId,
                firstName,
                lastName,
                phone,
                activeAddressId
        );
    }

    /// --- Getters ---
    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getPhone() { return phone; }

    public UUID getActiveAddressId() { return activeAddressId; }

    /// --- Business Logic ---
    private void validateData() {
        if (this.id == null) {
            throw new DomainException("ID_CANNOT_BE_NULL", "Profile Id is required.");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new DomainException("FIRST-NAME_CANNOT_BE_EMPTY", "Profile firstName cannot be empty.");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new DomainException("LAST-NAME_CANNOT_BE_EMPTY", "Profile lastName cannot be empty.");
        }
    }

    public void updateProfile(String firstName, String lastName, String phone) {
        if (firstName == null || firstName.isBlank()) {
            throw new DomainException("FIRST-NAME_CANNOT_BE_EMPTY", "Profile firstName cannot be empty.");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new DomainException("LAST-NAME_CANNOT_BE_EMPTY", "Profile lastName cannot be empty.");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public void setActiveAddress(UUID addressId) {
        this.activeAddressId = addressId;
    }
}
