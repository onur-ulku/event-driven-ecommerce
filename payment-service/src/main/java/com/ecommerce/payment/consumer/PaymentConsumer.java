package com.ecommerce.payment.consumer;

import com.ecommerce.common.kafka.KafkaGroups;
import com.ecommerce.common.kafka.KafkaTopics;
import com.ecommerce.common.event.StockReservedEvent;
import com.ecommerce.payment.service.PaymentService;
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
public class PaymentConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = KafkaTopics.STOCK_RESERVED, groupId = KafkaGroups.PAYMENT_SERVICE)
    public void handleStockReserved(
            @Payload StockReservedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Stock event received. OrderId: {}, Success: {}, Partition: {}, Offset: {}",
                event.getOrderId(), event.isSuccess(), partition, offset);

        paymentService.processPayment(event, event.getAmount());
    }
}
