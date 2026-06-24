package com.ecommerce.notification;

import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/*
 * In a real project this class would send an email, SMS, or push notification.
 * We simulate it with log messages.
 */
@Slf4j
@Service
public class NotificationConsumer {

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = "notification-service")
    public void handlePaymentCompleted(
            @Payload PaymentCompletedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Payment event received. OrderId: {}, Success: {}, Partition: {}, Offset: {}",
                event.getOrderId(), event.isSuccess(), partition, offset);

        if (event.isSuccess()) {
            log.info("[EMAIL] Order confirmed. OrderId: {}, CustomerId: {}, Amount: {}",
                    event.getOrderId(), event.getCustomerId(), event.getAmount());
        } else {
            log.warn("[EMAIL] Order failed. OrderId: {}, CustomerId: {}, Reason: {}",
                    event.getOrderId(), event.getCustomerId(), event.getFailureReason());
        }
    }
}
