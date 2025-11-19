package com.burakmert.notification_processor_service.service;

import com.burakmert.notification.avro.NotificationCreated;
import com.burakmert.notification.avro.NotificationRequest;
import com.burakmert.common.enums.NotificationType;
import com.burakmert.common.enums.NotificationChannel;
import com.burakmert.notification_processor_service.publisher.NotificationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProcessorService {

    private final NotificationEventPublisher notificationEventPublisher;

    public void process(NotificationRequest notificationRequest) {
        // Fan-out logic: emits notification created event per recipient
        notificationRequest.getRecipientIds().forEach(id -> {
            NotificationCreated notificationCreated = NotificationCreated
                    .newBuilder()
                    .setNotificationId(notificationRequest.getNotificationId())
                    .setRecipientId(id)
                    .setChannel(getChannelForNotificationType(NotificationType
                            .valueOf(notificationRequest.getType().toString()))
                            .toString())
                    .setTitle(notificationRequest.getTitle())
                    .setBody(notificationRequest.getBody())
                    .setData(notificationRequest.getData())
                    .setTimestamp(notificationRequest.getTimestamp())
                    .build();

            notificationEventPublisher.publish(notificationCreated);
        });
    }

    private NotificationChannel getChannelForNotificationType(NotificationType notificationType) {
        return switch (notificationType) {
            case COMMENT -> NotificationChannel.SMS;
            case SYSTEM_ALERT ->  NotificationChannel.EMAIL;
            default -> NotificationChannel.IN_APP;
        };
    }

}
