package com.burakmert.notification_api_service.publisher;

import com.burakmert.notification.avro.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRequestPublisher {

    @Value("${topics.notification-requests}")
    public String topic;

    public final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    public void publish(NotificationRequest request) {
        kafkaTemplate.send(topic, request.getNotificationId().toString(), request);
    }

}
