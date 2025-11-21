package com.burakmert.common.redis;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@Builder
public class NotificationPayload implements Serializable {

    private String recipientId;
    private String notificationId;
    private String title;
    private String body;
    private Map<String,Object> data;
    private Date createdAt;

}

