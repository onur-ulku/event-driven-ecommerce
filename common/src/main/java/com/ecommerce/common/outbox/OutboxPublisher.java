package com.ecommerce.common.outbox;

import com.ecommerce.common.event.PaymentCompletedEvent;
import com.ecommerce.common.event.StockReservedEvent;
import com.ecommerce.common.kafka.KafkaEventPublisher;
import com.ecommerce.common.kafka.KafkaTopics;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final Map<String, Class<?>> EVENT_TYPE_REGISTRY = Map.of(
            KafkaTopics.PAYMENT_COMPLETED, PaymentCompletedEvent.class,
            KafkaTopics.STOCK_RESERVED, StockReservedEvent.class
    );

    private final OutboxRepository outboxRepository;
    private final KafkaEventPublisher kafkaEventPublisher;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelayString = "${outbox.publisher.delay-ms:1000}")
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxRepository.findByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING,
                PageRequest.of(0, 100)
        );

        for (OutboxEvent event : events) {
            try {
                Class<?> eventType = EVENT_TYPE_REGISTRY.get(event.getTopic());
                Object payload = objectMapper.readValue(event.getPayload(), eventType);
                kafkaEventPublisher.publish(event.getTopic(), event.getKey(), payload);
                event.setStatus(OutboxStatus.SENT);
                outboxRepository.save(event);
                log.info("Outbox event published. Topic: {}, Key: {}", event.getTopic(), event.getKey());
            } catch (Exception e) {
                log.error("Failed to publish outbox event. Topic: {}, Key: {}, Error: {}",
                        event.getTopic(), event.getKey(), e.getMessage());
            }
        }
    }
}
