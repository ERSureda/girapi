package com.girlocal.girapi.shared.infraestructure.adapter.out.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {

    @Query(value = """
            SELECT *
            FROM outbox_events
            WHERE processed = false
            ORDER BY created_at ASC
            LIMIT :limit FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxEntity> findPendingEvents(@Param("limit") int limit);

    @Modifying
    @Query(value = """
            DELETE FROM outbox_events
            WHERE id IN (
                SELECT id
                FROM outbox_events
                WHERE processed = true AND processed_at < :cutoffDate
                LIMIT 1000 FOR UPDATE SKIP LOCKED
            )
            """, nativeQuery = true)
    int deleteProcessedEventsOlderThanInBatches(@Param("cutoffDate") Instant cutoffDate);
}
