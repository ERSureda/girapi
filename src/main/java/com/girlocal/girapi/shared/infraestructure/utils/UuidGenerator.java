package com.girlocal.girapi.shared.infraestructure.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public final class UuidGenerator {

    private static final long VERSION_7_BITS = 0x7000L;
    private static final long VARIANT_2_BITS = 0x8000000000000000L;
    private static final long MAX_SEQUENCE = 0xFFFL;
    private static final long MASK_SEQUENCE = 0xFFFL;
    private static final long MASK_RANDOM_BITS = 0x3FFFFFFFFFFFFFFFL;
    private static final long MASK_TIMESTAMP = 0xFFFFFFFFFFFFL;

    private static final int SEQUENCE_BITS = 12;
    private static final int TIMESTAMP_SHIFT = 16;

    private static final AtomicLong STATE = new AtomicLong();

    private UuidGenerator() {
        throw new UnsupportedOperationException("CAN_NOT_INSTANTIATE");
    }

    public static UUID generateId() {
        long timestamp;
        long sequence;
        long current;
        long next;

        do {
            current = STATE.get();
            long lastTimestamp = current >>> SEQUENCE_BITS;
            long now = System.currentTimeMillis();

            if (now > lastTimestamp) {
                timestamp = now;
                sequence = 0;
            } else {
                timestamp = lastTimestamp;
                sequence = (current & MASK_SEQUENCE) + 1;
                if (sequence > MAX_SEQUENCE) {
                    timestamp++;
                    sequence = 0;
                }
            }
            next = (timestamp << SEQUENCE_BITS) | sequence;
        } while (!STATE.compareAndSet(current, next));

        long msb = ((timestamp & MASK_TIMESTAMP) << TIMESTAMP_SHIFT) | VERSION_7_BITS | sequence;
        long lsb = (ThreadLocalRandom.current().nextLong() & MASK_RANDOM_BITS) | VARIANT_2_BITS;

        return new UUID(msb, lsb);
    }
}