package com.girlocal.girapi.shared.domain.event;

import java.time.Instant;
import java.util.UUID;

public record InitializeProfileEvent(
        UUID eventId,
        Instant occurredAt,
        String eventType,

        // --- Payload ---
        UUID userId,
        String firstName,
        String lastName
) implements DomainEvent {
    public InitializeProfileEvent(UUID userId, String firstName, String lastName) {
        this(
                UUID.randomUUID(),
                Instant.now(),
                "INITIALIZE_PROFILE",
                userId,
                firstName,
                lastName
        );
    }
}