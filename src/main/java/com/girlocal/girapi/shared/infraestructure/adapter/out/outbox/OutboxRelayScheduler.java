package com.girlocal.girapi.shared.infraestructure.adapter.out.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {

    private final OutboxRepository outboxRepository;
    // private final SnsPublisherPort snsPublisherPort;

    private static final int MAX_RETRIES = 5;
    private static final int BATCH_SIZE = 100;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processPendingEvents() {
        /* <OutboxEventEntity> pendingEvents = outboxRepository.findPendingEvents(BATCH_SIZE);

        if (pendingEvents.isEmpty()) return;

        for (OutboxEventEntity event : pendingEvents) {
            try {
                snsPublisherPort.publish(
                        event.getAggregateType(),
                        event.getAggregateId(),
                        event.getPayload());
                event.markAsProcessed();
            } catch (Exception e) {
                log.error("Failed to process outbox event with id: {}", event.getId(), e);
                event.markAsFailed(e.getMessage());

                if (event.getRetryCount() >= MAX_RETRIES) {
                    event.setProcessed(true);
                }
            }
        }*/
    }
}
