package com.ecommerce.stock;

import com.ecommerce.common.event.KafkaGroups;
import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/*
 * Responsible only for receiving messages from Kafka and delegating to StockService.
 * Business logic lives in StockService, not here.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockConsumer {

    private final StockService stockService;
    private final ProcessedOrderRepository processedOrderRepository;

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = KafkaGroups.STOCK_SERVICE)
    public void handleOrderCreated(
            @Payload OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Order received. OrderId: {}, ProductId: {}, Quantity: {}, Partition: {}, Offset: {}",
                event.getOrderId(), event.getProductId(), event.getQuantity(), partition, offset);

        if (processedOrderRepository.existsById(event.getOrderId())) {
            log.warn("Duplicate order detected, skipping. OrderId: {}", event.getOrderId());
            return;
        }

        processedOrderRepository.save(new ProcessedOrder(event.getOrderId(), LocalDateTime.now().toString()));
        stockService.reserve(event);
    }
}
