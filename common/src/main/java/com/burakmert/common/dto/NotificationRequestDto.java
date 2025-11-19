package com.burakmert.common.dto;

import com.burakmert.common.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Map;
import java.util.List;

@Data
public class NotificationRequestDto {
    @NotBlank(message = "actorId is required")
    private String actorId;

    @NotEmpty(message = "recipientIds cannot be empty")
    private List<String> recipientIds;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "notificationType is required")
    private NotificationType notificationType;

    // optional
    private String body;

    // dynamic metadata payload
    private Map<String, Object> data;
}
