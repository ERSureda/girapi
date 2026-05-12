package com.girlocal.girapi.shared.application.port.out;

import com.girlocal.girapi.shared.domain.event.DomainEvent;

import java.util.List;

public interface OutboxPublisherPort {
    void publish(DomainEvent event);
    void publishAll(List<DomainEvent> events);
}
