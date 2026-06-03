package com.girlocal.girapi.stores.domain.model;

import com.girlocal.girapi.stores.domain.model.valueobject.DaySchedule;
import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.model.BaseEntity;

import java.util.List;
import java.util.UUID;

public class StoreSchedule extends BaseEntity<UUID> {

    private final UUID storeId;
    private final List<DaySchedule> days;

    private StoreSchedule(UUID id, UUID storeId, List<DaySchedule> days) {
        this.id = id;
        this.storeId = storeId;
        this.days = days != null ? List.copyOf(days) : List.of();

        this.validateData();
    }

    public static StoreSchedule create(UUID id, UUID storeId, List<DaySchedule> days) {
        return new StoreSchedule(
                id != null ? id : UUID.randomUUID(),
                storeId,
                days
        );
    }

    public static StoreSchedule reconstruct(UUID id, UUID storeId, List<DaySchedule> days) {
        return new StoreSchedule(id, storeId, days);
    }

    /// --- Getters ---
    public UUID getStoreId() {
        return storeId;
    }

    public List<DaySchedule> getDays() {
        return days;
    }

    /// --- Business Logic ---
    private void validateData() {
        if (this.id == null) {
            throw new DomainException("ID_CANNOT_BE_NULL", "StoreSchedule Id is required.");
        }
        if (this.storeId == null) {
            throw new DomainException("STORE-ID_CANNOT_BE_NULL", "StoreSchedule storeId is required.");
        }
        if (this.days == null || this.days.isEmpty()) {
            throw new DomainException("DAYS_CANNOT_BE_EMPTY", "StoreSchedule days cannot be empty.");
        }
        // Validamos que no haya días duplicados
        long uniqueDays = this.days.stream().map(DaySchedule::dayOfWeek).distinct().count();
        if (uniqueDays != this.days.size()) {
            throw new DomainException("DAYS_DUPLICATED", "StoreSchedule days cannot have duplicates.");
        }
    }
}
