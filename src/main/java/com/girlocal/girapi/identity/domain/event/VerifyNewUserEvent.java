package com.girlocal.girapi.identity.domain.event;

import com.girlocal.girapi.identity.domain.model.enums.VerificationTokenType;
import com.girlocal.girapi.shared.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record VerifyNewUserEvent(
        UUID eventId,
        Instant occurredAt,
        String eventType,

        // --- Payload ---
        String email,
        String fullName,
        String token
) implements DomainEvent {
    public VerifyNewUserEvent(String email, String fullName, String token) {
        this(
                UUID.randomUUID(),
                Instant.now(),
                VerificationTokenType.VERIFY_NEW_USER.toString(),
                email,
                fullName,
                token
        );
    }
}
