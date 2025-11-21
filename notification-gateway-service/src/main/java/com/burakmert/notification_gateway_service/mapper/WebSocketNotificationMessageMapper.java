package com.burakmert.notification_gateway_service.mapper;

import com.burakmert.common.redis.NotificationPayload;
import com.burakmert.notification_gateway_service.model.WebSocketNotificationMessage;

public class WebSocketNotificationMessageMapper {

    public static WebSocketNotificationMessage createFromRedisPayload(NotificationPayload payload) {
        return WebSocketNotificationMessage
                .builder()
                .notificationId(payload.getNotificationId())
                .recipientId(payload.getRecipientId())
                .title(payload.getTitle())
                .body(payload.getBody())
                .createdAt(payload.getCreatedAt())
                .build();
    }

}
