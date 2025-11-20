package com.burakmert.notification_query_service.model;

import com.burakmert.common.enums.NotificationChannel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "notification")
@Data
@Builder
public class Notification {

    @Id
    private String id;

    private String recipientId;
    private NotificationChannel channel;
    private String title;
    private String body;
    private Map<String, Object> data;

    @CreatedDate
    private Date createdAt;
    private boolean isRead;
    private Date readAt;

}
