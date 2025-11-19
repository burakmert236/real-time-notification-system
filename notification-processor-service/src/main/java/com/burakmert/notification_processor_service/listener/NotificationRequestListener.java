package com.burakmert.notification_processor_service.listener;

import com.burakmert.notification.avro.NotificationRequest;
import com.burakmert.notification_processor_service.service.NotificationProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRequestListener {

    private final NotificationProcessorService notificationProcessorService;

    @KafkaListener(topics = "${topics.notification-requests}")
    public void listen(NotificationRequest notificationRequest) {
        notificationProcessorService.process(notificationRequest);
    }

}
