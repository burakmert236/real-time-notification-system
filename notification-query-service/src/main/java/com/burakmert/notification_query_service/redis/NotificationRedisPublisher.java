package com.burakmert.notification_query_service.redis;

import com.burakmert.common.redis.NotificationPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRedisPublisher {

    @Value("${redis-channels.in-app-notifications}")
    private String redisChannel;

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(NotificationPayload payload) {
        redisTemplate.convertAndSend(redisChannel, payload);
    }

}
