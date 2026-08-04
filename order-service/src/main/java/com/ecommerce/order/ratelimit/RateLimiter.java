package com.ecommerce.order.ratelimit;

import com.ecommerce.order.config.RateLimitProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimiter {

    private static final String KEY_PREFIX = "rate:order:";

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties properties;

    public boolean tryAcquire(String customerId) {
        String key = KEY_PREFIX + customerId;
        long now = System.currentTimeMillis();
        long windowStart = now - Duration.ofSeconds(properties.getWindowSeconds()).toMillis();

        redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart);

        Long count = redisTemplate.opsForZSet().zCard(key);
        long current = (count == null) ? 0 : count;

        if (current >= properties.getLimit()) {
            log.warn("Rate limit exceeded. CustomerId: {}, Count: {}, Limit: {}",
                    customerId, current, properties.getLimit());
            return false;
        }

            redisTemplate.opsForZSet().add(key, UUID.randomUUID().toString(), now);
        redisTemplate.expire(key, Duration.ofSeconds(properties.getWindowSeconds()));

        return true;
    }
}
