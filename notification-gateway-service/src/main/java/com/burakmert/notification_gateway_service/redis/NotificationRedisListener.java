package com.burakmert.notification_gateway_service.redis;

import com.burakmert.common.redis.NotificationPayload;
import com.burakmert.notification_gateway_service.mapper.WebSocketNotificationMessageMapper;
import com.burakmert.notification_gateway_service.model.WebSocketNotificationMessage;
import com.burakmert.notification_gateway_service.websocket.ConnectionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRedisListener implements MessageListener {

    private final ConnectionManager connectionManager;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            NotificationPayload payload =
                    objectMapper.readValue(json, NotificationPayload.class);

            log.info("Received NotificationPayload: {}", payload);

            WebSocketNotificationMessage notificationMessage = WebSocketNotificationMessageMapper.createFromRedisPayload(payload);
            String messageString = objectMapper.writeValueAsString(notificationMessage);

            List<WebSocketSession> sessions = connectionManager.getSessions(payload.getRecipientId());
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(messageString));
                    } catch (IOException e) {
                        log.warn("Failed to send WS message to session {}: {}", session.getId(), e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to deserialize Redis message", e);
        }
    }
}
