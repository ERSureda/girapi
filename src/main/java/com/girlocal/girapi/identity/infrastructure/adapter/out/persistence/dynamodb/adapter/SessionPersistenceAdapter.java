package com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.dynamodb.adapter;

import com.girlocal.girapi.identity.application.port.out.SessionPort;
import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.dynamodb.entity.SessionEntity;
import com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.dynamodb.mapper.SessionMapper;
import com.girlocal.girapi.shared.domain.model.AuthenticatedUser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionPersistenceAdapter implements SessionPort {

    private static final int DYNAMO_BATCH_WRITE_LIMIT = 25;
    private static final int MAX_BATCH_RETRIES = 3;
    private static final int MAX_ACTIVE_SESSIONS = 50;

    private final DynamoDbEnhancedClient enhancedClient;
    private final SessionMapper sessionMapper;

    @Value("${application.dynamodb.table-name:user_sessions}")
    private String tableName;

    private DynamoDbTable<SessionEntity> table;

    @PostConstruct
    void init() {
        this.table = enhancedClient.table(tableName, TableSchema.fromBean(SessionEntity.class));
    }

    @Override
    public Optional<AuthenticatedUser> getSessionById(String token) {
        Key key = Key.builder()
                .partitionValue(SessionEntity.PK_PREFIX + token)
                .sortValue(SessionEntity.SK_META)
                .build();

        SessionEntity item = table.getItem(key);

        if (item == null) {
            log.debug("Session not found for token: {}", token);
            return Optional.empty();
        }

        if (item.getTtl() != null && Instant.now().getEpochSecond() > item.getTtl()) {
            log.debug("Session in TTL grace period for token: {}. Ignoring.", token);
            return Optional.empty();
        }

        return Optional.of(sessionMapper.toDomain(item));
    }

    @Override
    public List<AuthenticatedUser> getSessionsByUserId(String userId) {
        DynamoDbIndex<SessionEntity> index = table.index(SessionEntity.GSI_USER_ID);

        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(userId).build()
        );

        long nowEpoch = Instant.now().getEpochSecond();

        // Filter expired sessions server-side to reduce data transfer and RCU
        Expression ttlFilter = Expression.builder()
                .expression("attribute_not_exists(#ttl) OR #ttl > :now")
                .putExpressionName("#ttl", "ttl")
                .putExpressionValue(":now", AttributeValue.builder().n(String.valueOf(nowEpoch)).build())
                .build();

        List<AuthenticatedUser> sessions = index.query(QueryEnhancedRequest.builder()
                        .queryConditional(queryConditional)
                        .filterExpression(ttlFilter)
                        .limit(MAX_ACTIVE_SESSIONS)
                        .build())
                .stream()
                .flatMap(page -> page.items().stream())
                .limit(MAX_ACTIVE_SESSIONS)
                .map(sessionMapper::toDomain)
                .toList();

        log.debug("Found {} active sessions for user: {}", sessions.size(), userId);
        return Collections.unmodifiableList(sessions);
    }

    @Override
    public void saveSession(String token, AuthenticatedUser authenticatedUser, long expirationEpoch) {
        SessionEntity entity = sessionMapper.toEntity(authenticatedUser);
        entity.setPk(SessionEntity.PK_PREFIX + token);
        entity.setSk(SessionEntity.SK_META);
        entity.setTtl(expirationEpoch);

        table.putItem(entity);
        log.info("Session saved for user: {} with TTL: {}", authenticatedUser.userId(), expirationEpoch);
    }

    @Override
    public void deleteSessionById(String token) {
        Key key = Key.builder()
                .partitionValue(SessionEntity.PK_PREFIX + token)
                .sortValue(SessionEntity.SK_META)
                .build();

        table.deleteItem(key);
        log.debug("Session deleted for token: {}", token);
    }

    @Override
    public void deleteAllSessionsByUserId(String userId) {
        DynamoDbIndex<SessionEntity> index = table.index(SessionEntity.GSI_USER_ID);

        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(userId).build()
        );

        int totalDeleted = 0;

        for (var page : index.query(QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build())) {

            List<SessionEntity> pageItems = page.items();
            if (pageItems.isEmpty()) {
                continue;
            }

            for (int i = 0; i < pageItems.size(); i += DYNAMO_BATCH_WRITE_LIMIT) {
                List<SessionEntity> batch = pageItems.subList(
                        i, Math.min(i + DYNAMO_BATCH_WRITE_LIMIT, pageItems.size()));

                totalDeleted += executeBatchDeleteWithRetry(batch);
            }
        }

        if (totalDeleted == 0) {
            log.debug("No sessions found to delete for user: {}", userId);
        } else {
            log.info("All sessions deleted for user: {}. Total: {}", userId, totalDeleted);
        }
    }

    /**
     * Executes a batch delete with retries for unprocessed items (DynamoDB throttling).
     * Uses exponential backoff between retries.
     *
     * @param items the items to delete in a single batch (max 25).
     * @return the number of items that were actually deleted.
     */
    private int executeBatchDeleteWithRetry(List<SessionEntity> items) {
        int totalRequested = items.size();

        WriteBatch.Builder<SessionEntity> batchBuilder = WriteBatch.builder(SessionEntity.class)
                .mappedTableResource(table);
        items.forEach(batchBuilder::addDeleteItem);

        BatchWriteResult result = enhancedClient.batchWriteItem(
                BatchWriteItemEnhancedRequest.builder()
                        .writeBatches(batchBuilder.build())
                        .build());

        List<Key> pendingKeys = result.unprocessedDeleteItemsForTable(table);

        for (int attempt = 1; attempt < MAX_BATCH_RETRIES && !pendingKeys.isEmpty(); attempt++) {
            try {
                long backoffMs = 50L * (1L << (2 * attempt)); // 200ms, 800ms
                TimeUnit.MILLISECONDS.sleep(backoffMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Retry interrupted during batch delete, {} items still pending", pendingKeys.size());
                return totalRequested - pendingKeys.size();
            }

            log.debug("Retrying batch delete, attempt {}/{}, pending items: {}",
                    attempt + 1, MAX_BATCH_RETRIES, pendingKeys.size());

            WriteBatch.Builder<SessionEntity> retryBuilder = WriteBatch.builder(SessionEntity.class)
                    .mappedTableResource(table);
            pendingKeys.forEach(retryBuilder::addDeleteItem);

            result = enhancedClient.batchWriteItem(
                    BatchWriteItemEnhancedRequest.builder()
                            .writeBatches(retryBuilder.build())
                            .build());

            pendingKeys = result.unprocessedDeleteItemsForTable(table);
        }

        if (!pendingKeys.isEmpty()) {
            log.warn("Failed to delete {} out of {} items after {} retries",
                    pendingKeys.size(), totalRequested, MAX_BATCH_RETRIES);
        }

        return totalRequested - pendingKeys.size();
    }
}
