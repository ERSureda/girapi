package com.girlocal.girapi.identity.domain.event;

import com.girlocal.girapi.identity.domain.model.enums.VerificationTokenType;
import com.girlocal.girapi.shared.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ResetPasswordEvent(
        UUID eventId,
        Instant occurredAt,
        String eventType,

        // --- Payload ---
        String email,
        String token
) implements DomainEvent {
    public ResetPasswordEvent(String email, String token) {
        this(
                UUID.randomUUID(),
                Instant.now(),
                VerificationTokenType.PASSWORD_RESET.toString(),
                email,
                token
        );
    }
}
