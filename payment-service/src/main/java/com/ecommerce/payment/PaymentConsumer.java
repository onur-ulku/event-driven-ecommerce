package com.ecommerce.payment;

import com.ecommerce.common.event.KafkaGroups;
import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.PaymentCompletedEvent;
import com.ecommerce.common.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;

    @KafkaListener(topics = KafkaTopics.STOCK_RESERVED, groupId = KafkaGroups.PAYMENT_SERVICE)
    public void handleStockReserved(
            @Payload StockReservedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Stock event received. OrderId: {}, Success: {}, Partition: {}, Offset: {}",
                event.getOrderId(), event.isSuccess(), partition, offset);

        PaymentResult result = paymentService.processPayment(event, event.getAmount());

        PaymentCompletedEvent paymentEvent = new PaymentCompletedEvent(
                event.getOrderId(),
                event.getCustomerId(),
                event.getAmount(),
                result.isSuccess(),
                result.getFailure(),
                Instant.now().toString()
        );

        paymentProducer.sendPaymentCompletedEvent(paymentEvent);
    }
}
