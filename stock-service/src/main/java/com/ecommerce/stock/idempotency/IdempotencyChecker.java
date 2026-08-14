package com.ecommerce.stock.idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyChecker {

    private static final String KEY_PREFIX = "processed:order:";

    private final StringRedisTemplate redisTemplate;

    @Value("${idempotency.ttl-hours:24}")
    private long ttlHours;

    public boolean isFirstProcessing(String orderId) {
        Boolean firstTime = redisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + orderId, LocalDateTime.now().toString(), Duration.ofHours(ttlHours));
        return Boolean.TRUE.equals(firstTime);
    }
}
