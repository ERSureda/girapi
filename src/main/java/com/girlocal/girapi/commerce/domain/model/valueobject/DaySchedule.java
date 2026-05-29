package com.girlocal.girapi.commerce.domain.model.valueobject;

import com.girlocal.girapi.shared.domain.exception.DomainException;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record DaySchedule(
        DayOfWeek dayOfWeek,
        LocalTime openTime,
        LocalTime closeTime,
        boolean isClosed
) {
    public DaySchedule {
        if (dayOfWeek == null) {
            throw new DomainException("DAY_OF_WEEK_REQUIRED", "Day of week is required.");
        }
        if (!isClosed) {
            if (openTime == null || closeTime == null) {
                throw new DomainException("SCHEDULE_TIMES_REQUIRED", "Open and close times are required when not closed.");
            }
            if (openTime.isAfter(closeTime) || openTime.equals(closeTime)) {
                throw new DomainException("INVALID_SCHEDULE_HOURS", "Open time must be before close time.");
            }
        }
    }
}
