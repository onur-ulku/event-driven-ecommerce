package com.ecommerce.stock;

import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.common.event.StockReservedEvent;
import com.ecommerce.common.outbox.OutboxEvent;
import com.ecommerce.common.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void reserve(OrderCreatedEvent event) {
        Optional<StockItem> optional = stockRepository.findByProductId(event.getProductId());

        StockReservedEvent stockReservedEvent;

        if (optional.isEmpty()) {
            log.warn("Product not found. ProductId: {}", event.getProductId());
            stockReservedEvent = buildStockEvent(event, false, "Product not found");
        } else {
            StockItem item = optional.get();
            if (!item.hasEnoughStock(event.getQuantity())) {
                log.warn("Insufficient stock. ProductId: {}, Available: {}, Requested: {}",
                        event.getProductId(), item.getQuantity(), event.getQuantity());
                stockReservedEvent = buildStockEvent(event, false, "Insufficient stock");
            } else {
                item.decrease(event.getQuantity());
                stockRepository.save(item);
                log.info("Stock reserved. ProductId: {}, Remaining: {}", event.getProductId(), item.getQuantity());
                stockReservedEvent = buildStockEvent(event, true, null);
            }
        }

        saveToOutbox(stockReservedEvent);
    }

    private StockReservedEvent buildStockEvent(OrderCreatedEvent event, boolean success, String failureReason) {
        return new StockReservedEvent(
                event.getOrderId(),
                event.getCustomerId(),
                event.getProductId(),
                event.getQuantity(),
                event.getPrice(),
                success,
                failureReason,
                LocalDateTime.now().toString()
        );
    }

    private void saveToOutbox(StockReservedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            outboxRepository.save(new OutboxEvent(
                    KafkaTopics.STOCK_RESERVED,
                    event.getOrderId(),
                    payload,
                    LocalDateTime.now().toString()
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize stock event", e);
        }
    }
}
