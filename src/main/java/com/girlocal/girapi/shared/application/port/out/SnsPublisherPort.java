package com.girlocal.girapi.shared.application.port.out;

public interface SnsPublisherPort {
    void publish(String aggregateType, String aggregateId, String payload);
}
