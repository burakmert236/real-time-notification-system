package com.burakmert.notification_gateway_service.websocket;

import com.burakmert.notification_gateway_service.auth.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ConnectionManager connectionManager;
    private final JwtAuthenticationService jwtAuthenticationService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = extractToken(session.getUri());
        if (token == null || token.isBlank()) {
            log.warn("Missing token, closing session {}", session.getId());
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing token"));
            return;
        }

        try {
            String userId = jwtAuthenticationService.extractUserId(token);
            session.getAttributes().put("userId", userId);
            connectionManager.addSession(userId, session);
            log.info("WS connection established for user {} session {}", userId, session.getId());

            // send a welcome message
            session.sendMessage(new TextMessage("{\"type\":\"CONNECTED\"}"));

        } catch (Exception e) {
            log.warn("Invalid token, closing session {}: {}", session.getId(), e.getMessage());
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Optionally handle client messages (ping, mark-read, etc.)
        log.debug("Received msg from {}: {}", session.getId(), message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Object userIdObj = session.getAttributes().get("userId");
        if (userIdObj != null) {
            String userId = userIdObj.toString();
            connectionManager.removeSession(userId, session);
        }
        log.info("WS session {} closed: {}", session.getId(), status);
    }

    private String extractToken(URI uri) {
        if (uri == null) return null;
        var components = UriComponentsBuilder.fromUri(uri).build();
        List<String> tokens = components.getQueryParams().get("token");
        return (tokens == null || tokens.isEmpty()) ? null : tokens.get(0);
    }
}
