package com.burakmert.notification_query_service.service;

import com.burakmert.common.enums.NotificationChannel;
import com.burakmert.common.redis.NotificationPayload;
import com.burakmert.notification_query_service.model.Notification;
import com.burakmert.notification.avro.NotificationCreated;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    public Notification createFrom(NotificationCreated notificationCreatedEvent) {
        return Notification
                .builder()
                .notificationRequestId(notificationCreatedEvent.getNotificationId().toString())
                .recipientId(notificationCreatedEvent.getRecipientId().toString())
                .channel(NotificationChannel.valueOf(notificationCreatedEvent.getChannel().toString()))
                .title(notificationCreatedEvent.getTitle().toString())
                .body(notificationCreatedEvent.getBody().toString())
                .data(Optional.ofNullable(notificationCreatedEvent.getData())
                        .orElseGet(Collections::emptyMap).entrySet().stream()
                        .collect(Collectors.toMap(
                                k -> k.getKey().toString(),
                                e -> String.valueOf(e.getValue())
                        )))
                .isRead(false)
                .build();
    }

    public NotificationPayload createPayloadFrom(NotificationCreated notificationCreated) {
        return NotificationPayload
                .builder()
                .recipientId(notificationCreated.getRecipientId().toString())
                .notificationId(notificationCreated.getNotificationId().toString())
                .title(notificationCreated.getTitle().toString())
                .body(notificationCreated.getBody().toString())
                .data(Optional.ofNullable(notificationCreated.getData())
                        .orElseGet(Collections::emptyMap).entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey().toString(),
                                e -> String.valueOf(e.getValue())
                        )))
                .build();
    }

}
