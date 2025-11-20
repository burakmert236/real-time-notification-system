package com.burakmert.notification_query_service.controller;

import com.burakmert.notification_query_service.exception.NotificationNotFoundException;
import com.burakmert.notification_query_service.model.Notification;
import com.burakmert.notification_query_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable String id) {
        return notificationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotificationNotFoundException("Notification with id " + id + " not found"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getAllByUserId(@PathVariable String userId,
                                                             @RequestParam(required = false) Optional<Boolean> isRead) {
        return isRead.map(aBoolean -> ResponseEntity
                .ok(notificationRepository.findAllByRecipientIdAndIsReadOrderByCreatedAt(userId, aBoolean)))
                .orElseGet(() -> ResponseEntity.ok(notificationRepository.findAllByRecipientIdOrderByCreatedAt(userId)));
    }

    @PostMapping("/{userId}/read")
    public ResponseEntity<String> read(@PathVariable String userId) {
        long result = notificationRepository.updateIsReadByRecipientId(userId, true);
        return ResponseEntity.ok(result + " notifications marked as read successfully");
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(NotificationNotFoundException ex) {
        ErrorResponse error = ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
