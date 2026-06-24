package com.ecommerce.stock;

import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.StockReservedEvent;
import com.ecommerce.common.kafka.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockProducer {

    private final KafkaEventPublisher kafkaEventPublisher;

    public void sendStockReservedEvent(StockReservedEvent event) {
        kafkaEventPublisher.publish(KafkaTopics.STOCK_RESERVED, event.getOrderId(), event);
    }
}
