package com.ecommerce.stock;

import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockProducer {

    private final KafkaTemplate<String, StockReservedEvent> kafkaTemplate;

    /*
     * Sends a stock-reserved event after stock processing is complete.
     * Payment Service listens to this event to initiate the payment flow.
     * stock-service has no knowledge of payment-service — it only writes to Kafka.
     */
    public void sendStockReservedEvent(StockReservedEvent event) {
        kafkaTemplate.send(KafkaTopics.STOCK_RESERVED, event.getOrderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send stock event. OrderId: {}, Error: {}",
                                event.getOrderId(), ex.getMessage());
                    } else {
                        log.info("Stock event sent. OrderId: {}, Success: {}, Partition: {}, Offset: {}",
                                event.getOrderId(),
                                event.isSuccess(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
