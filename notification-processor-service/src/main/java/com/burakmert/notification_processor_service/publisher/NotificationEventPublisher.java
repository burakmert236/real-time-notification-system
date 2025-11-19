package com.burakmert.notification_processor_service.publisher;

import com.burakmert.notification.avro.NotificationCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    @Value("${topics.notification-events}")
    private String topic;

    private final KafkaTemplate<String, NotificationCreated> kafkaTemplate;

    public void publish(NotificationCreated event) {
        kafkaTemplate.send(topic, event.getNotificationId().toString(), event);
    }

}
