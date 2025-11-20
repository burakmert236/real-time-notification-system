package com.burakmert.notification_query_service.listener;

import com.burakmert.notification.avro.NotificationCreated;
import com.burakmert.notification_query_service.model.Notification;
import com.burakmert.notification_query_service.repository.NotificationRepository;
import com.burakmert.notification_query_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${topics.notification-events}")
    public void listen(NotificationCreated notificationCreated) {
        Notification notification = notificationService.createFrom(notificationCreated);
        notificationRepository.save(notification);
    }

}
