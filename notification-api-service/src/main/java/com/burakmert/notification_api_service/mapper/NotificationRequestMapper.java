package com.burakmert.notification_api_service.mapper;

import com.burakmert.notification.avro.NotificationRequest;
import com.burakmert.common.dto.NotificationRequestDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NotificationRequestMapper {

    public NotificationRequest toAvro(NotificationRequestDto notificationRequestDto) {
        return NotificationRequest
                .newBuilder()
                .setNotificationId(UUID.randomUUID().toString())
                .setType(notificationRequestDto.getNotificationType().toString())
                .setActorId(notificationRequestDto.getActorId())
                .setRecipientIds(notificationRequestDto.getRecipientIds().stream()
                        .map(s -> (CharSequence) s)
                        .toList())
                .setTitle(notificationRequestDto.getTitle())
                .setBody(notificationRequestDto.getBody())
                .setData(Optional.ofNullable(notificationRequestDto.getData())
                        .orElseGet(Collections::emptyMap).entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> String.valueOf(e.getValue())
                        )))
                .setTimestamp(Instant.now().getNano())
                .build();
    }

}
