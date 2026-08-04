package com.ecommerce.stock.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfig {

    public static final String STOCK_CACHE = "stock";

    private final CacheProperties cacheProperties;

    // @Primary: two CacheManagers exist (one comes from the Redisson starter).
    // On type conflict, Spring picks the @Primary one — this Spring RedisCacheManager.
    @Bean
    @Primary
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          ObjectMapper objectMapper) {
        RedisCacheConfiguration base = baseConfiguration(objectMapper);

        Map<String, RedisCacheConfiguration> perCacheConfigs = new HashMap<>();
        cacheProperties.getTtls().forEach((cacheName, ttl) ->
                perCacheConfigs.put(cacheName, base.entryTtl(ttl)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(base)
                .withInitialCacheConfigurations(perCacheConfigs)
                .build();
    }

    private RedisCacheConfiguration baseConfiguration(ObjectMapper objectMapper) {
        ObjectMapper cacheMapper = objectMapper.copy();
        cacheMapper.activateDefaultTyping(
                cacheMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY);

        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(cacheMapper)));
    }
}
