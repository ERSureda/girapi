package com.girlocal.girapi.identity.domain.event;

import com.girlocal.girapi.identity.domain.model.enums.VerificationTokenType;
import com.girlocal.girapi.shared.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ForgotPasswordEvent(
        UUID eventId,
        Instant occurredAt,
        String eventType,

        // --- Payload ---
        String email,
        String token
) implements DomainEvent {
    public ForgotPasswordEvent(String email, String token) {
        this(
                UUID.randomUUID(),
                Instant.now(),
                VerificationTokenType.FORGOT_PASSWORD.toString(),
                email,
                token
        );
    }
}
