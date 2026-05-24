package com.girlocal.girapi.identity.domain.model;

import com.girlocal.girapi.identity.domain.model.enums.VerificationTokenType;
import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.model.BaseEntity;

import java.time.Instant;
import java.util.UUID;

public class VerificationToken extends BaseEntity<UUID> {

    private final UUID userId;
    private final VerificationTokenType type;
    private final Instant expiresAt;

    /// --- Constructors ---
    private VerificationToken(
            UUID id,
            UUID userId,
            VerificationTokenType type,
            Instant expiresAt
    ) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.expiresAt = expiresAt;

        this.validateData();
    }

    public static VerificationToken create(
            UUID id,
            UUID userId,
            VerificationTokenType type,
            Instant expiresAt
    ) {
        return new VerificationToken(
                id,
                userId,
                type,
                expiresAt
        );
    }

    public static VerificationToken reconstruct(
            UUID token,
            UUID userId,
            VerificationTokenType type,
            Instant expiresAt
    ) {
        return new VerificationToken(
                token,
                userId,
                type,
                expiresAt
        );
    }

    /// --- Getters ---
    public UUID getToken() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public VerificationTokenType getType() {
        return type;
    }

    public Instant getExpiryDate() {
        return expiresAt;
    }

    /// --- Business Logic ---
    private void validateData() {
        if (id == null)
            throw new DomainException("ID_CANNOT_BE_NULL", "Token Id is required.");
        if (userId == null)
            throw new DomainException("USER-ID_CANNOT_BE_NULL", "Token userId is required.");
        if (type == null)
            throw new DomainException("TYPE_CANNOT_BE_NULL", "Token type is required.");
        if (expiresAt == null)
            throw new DomainException("EXPIRY-DATE_CANNOT_BE_NULL", "Token expiry date is required.");
    }

    public void validateNotExpired() {
        if (Instant.now().isAfter(this.expiresAt))
            throw new DomainException("TOKEN_EXPIRED", "Token has expired.");
    }
}
