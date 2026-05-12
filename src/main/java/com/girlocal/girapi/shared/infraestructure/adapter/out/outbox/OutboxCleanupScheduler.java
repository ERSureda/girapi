package com.girlocal.girapi.shared.infraestructure.adapter.out.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxCleanupScheduler {

    private final OutboxRepository outboxRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupProcessedEvents() {
        Instant cutoffDate = Instant.now().minus(7, ChronoUnit.DAYS);

        int deletedCount;
        int totalDeleted = 0;

        log.debug("Starting cleanup of outbox events processed before {}", cutoffDate);

        do {
            deletedCount = outboxRepository.deleteProcessedEventsOlderThanInBatches(cutoffDate);
            totalDeleted += deletedCount;
        } while (deletedCount == 1000);

        if (totalDeleted > 0) {
            log.info("Successfully deleted {} processed outbox events older than {}", totalDeleted, cutoffDate);
        }
    }
}
