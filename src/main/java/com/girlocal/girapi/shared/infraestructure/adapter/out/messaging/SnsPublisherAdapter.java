package com.girlocal.girapi.shared.infraestructure.adapter.out.messaging;

import com.girlocal.girapi.shared.application.port.out.SnsPublisherPort;
import com.girlocal.girapi.shared.domain.exception.InfrastructureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.Map;

@Slf4j
@Component
public class SnsPublisherAdapter implements SnsPublisherPort {

    private final SnsClient snsClient;
    private final String topicArn;

    public SnsPublisherAdapter(
            SnsClient snsClient,
            @Value("${application.sns.topic-name:local-events}") String topicArn
    ) {
        this.snsClient = snsClient;
        this.topicArn = topicArn;
    }

    @Override
    public void publish(String aggregateType, String aggregateId, String payload) {
        log.debug("Publishing event to SNS: aggregateType={}, aggregateId={}", aggregateType, aggregateId);
        try {
            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(payload)
                    // messageGroupId(aggregateId) — enable only for FIFO topics (ARN ends in .fifo)
                    .messageAttributes(Map.of(
                            "aggregateType", MessageAttributeValue.builder()
                                    .dataType("String")
                                    .stringValue(aggregateType)
                                    .build()
                    ))
                    .build();

            snsClient.publish(request);
            log.info("Published event '{}' for aggregate '{}' to SNS", aggregateType, aggregateId);
        } catch (Exception e) {
            log.error("Failed to publish event to SNS: aggregateType={}, aggregateId={}", aggregateType, aggregateId, e);
            throw new InfrastructureException("FAILED_PUBLISH_EVENT", "Failed to publish event to SNS.");
        }
    }
}
