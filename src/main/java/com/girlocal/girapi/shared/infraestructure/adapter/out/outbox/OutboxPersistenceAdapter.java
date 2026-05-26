package com.girlocal.girapi.shared.infraestructure.adapter.out.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.girlocal.girapi.shared.application.port.out.OutboxPublisherPort;
import com.girlocal.girapi.shared.domain.event.DomainEvent;
import com.girlocal.girapi.shared.domain.exception.InfrastructureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPersistenceAdapter implements OutboxPublisherPort {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(DomainEvent event) {
        try {
            OutboxEntity outboxEvent = new OutboxEntity(
                    event.getClass().getSimpleName(),
                    event.eventId().toString(),
                    objectMapper.writeValueAsString(event)
            );
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize domain event of type {}.", event.getClass().getSimpleName());
            throw new InfrastructureException("SERIALIZATION_ERROR", "Failed to serialize domain event.");
        }
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) return;
        List<OutboxEntity> entities = events.stream()
                .map(event -> {
                    try {
                        return new OutboxEntity(
                                event.getClass().getSimpleName(),
                                event.eventId().toString(),
                                objectMapper.writeValueAsString(event)
                        );
                    } catch (JsonProcessingException e) {
                        log.error("Failed to serialize domain event of type {}.", event.getClass().getSimpleName(), e);
                        throw new InfrastructureException("SERIALIZATION_ERROR", "Failed to serialize domain event.");
                    }
                })
                .toList();
        outboxRepository.saveAll(entities);
    }
}
