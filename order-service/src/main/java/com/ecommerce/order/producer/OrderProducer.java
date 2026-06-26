package com.ecommerce.order.producer;

import com.ecommerce.common.kafka.KafkaTopics;
import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.common.kafka.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaEventPublisher kafkaEventPublisher;

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        kafkaEventPublisher.publish(KafkaTopics.ORDER_CREATED, event.getOrderId(), event);
    }
}
