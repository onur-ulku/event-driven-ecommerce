package com.ecommerce.stock.consumer;

import com.ecommerce.common.kafka.KafkaGroups;
import com.ecommerce.common.event.PaymentCompletedEvent;
import com.ecommerce.common.kafka.KafkaTopics;
import com.ecommerce.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentResultConsumer {

    private final StockService stockService;

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = KafkaGroups.STOCK_SERVICE)
    public void handlePaymentCompleted(
            @Payload PaymentCompletedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Payment result received. OrderId: {}, Success: {}, Partition: {}, Offset: {}",
                event.getOrderId(), event.isSuccess(), partition, offset);

        if (!event.isSuccess()) {
            log.warn("Payment failed, compensating stock. OrderId: {}, ProductId: {}, Quantity: {}",
                    event.getOrderId(), event.getProductId(), event.getQuantity());
            stockService.compensate(event.getProductId(), event.getQuantity());
        }
    }
}
