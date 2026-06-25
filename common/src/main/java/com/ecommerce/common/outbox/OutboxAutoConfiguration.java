package com.ecommerce.common.outbox;

import com.ecommerce.common.kafka.KafkaEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigurationPackage(basePackages = "com.ecommerce.common.outbox")
public class OutboxAutoConfiguration {

    @Bean
    public OutboxPublisher outboxPublisher(OutboxRepository outboxRepository,
                                           KafkaEventPublisher kafkaEventPublisher,
                                           ObjectMapper objectMapper) {
        return new OutboxPublisher(outboxRepository, kafkaEventPublisher, objectMapper);
    }
}
