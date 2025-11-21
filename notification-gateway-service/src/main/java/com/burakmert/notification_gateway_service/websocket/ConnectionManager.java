package com.burakmert.notification_gateway_service.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class ConnectionManager {

    private final Map<String, List<WebSocketSession>> sessionsByUser = new ConcurrentHashMap<>();

    public void addSession(String userId, WebSocketSession session) {
        sessionsByUser
                .computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
                .add(session);
        log.info("Added WS session {} for user {}", session.getId(), userId);
    }

    public void removeSession(String userId, WebSocketSession session) {
        List<WebSocketSession> sessions = sessionsByUser.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            log.info("Removed WS session {} for user {}", session.getId(), userId);
            if (sessions.isEmpty()) {
                sessionsByUser.remove(userId);
                log.info("User {} has no more active sessions", userId);
            }
        }
    }

    public List<WebSocketSession> getSessions(String userId) {
        return sessionsByUser.getOrDefault(userId, List.of());
    }
}