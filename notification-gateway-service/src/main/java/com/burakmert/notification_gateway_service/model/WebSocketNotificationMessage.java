package com.burakmert.notification_gateway_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketNotificationMessage {

    private String notificationId;
    private String recipientId;
    private String title;
    private String body;
    private Map<String, Object> data;
    private Date createdAt;

}
