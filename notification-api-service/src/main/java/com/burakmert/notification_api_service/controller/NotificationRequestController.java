package com.burakmert.notification_api_service.controller;

import com.burakmert.common.dto.NotificationRequestDto;
import com.burakmert.notification_api_service.mapper.NotificationRequestMapper;
import com.burakmert.notification_api_service.publisher.NotificationRequestPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("notifications")
public class NotificationRequestController {

    private final NotificationRequestMapper mapper;
    private final NotificationRequestPublisher publisher;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping
    public ResponseEntity<String> submitNotificationRequest(@Valid @RequestBody NotificationRequestDto notificationRequestDto) {
        publisher.publish(mapper.toAvro(notificationRequestDto));
        return ResponseEntity.ok("Notification Request Submitted");
    }

}
