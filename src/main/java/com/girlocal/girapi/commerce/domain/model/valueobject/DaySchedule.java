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
            throw new DomainException("DAY-OF-WEEK_CANNOT_BE_NULL", "DaySchedule dayOfWeek is required.");
        }
        if (!isClosed) {
            if (openTime == null) {
                throw new DomainException("OPEN-TIME_CANNOT_BE_NULL", "DaySchedule openTime is required when not closed.");
            }
            if (closeTime == null) {
                throw new DomainException("CLOSE-TIME_CANNOT_BE_NULL", "DaySchedule closeTime is required when not closed.");
            }
            if (openTime.isAfter(closeTime) || openTime.equals(closeTime)) {
                throw new DomainException("TIMES_INVALID", "DaySchedule openTime must be before closeTime.");
            }
        }
    }
}
