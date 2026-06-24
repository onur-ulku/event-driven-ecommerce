package com.ecommerce.payment;

import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.PaymentCompletedEvent;
import com.ecommerce.common.kafka.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaEventPublisher kafkaEventPublisher;

    public void sendPaymentCompletedEvent(PaymentCompletedEvent event) {
        kafkaEventPublisher.publish(KafkaTopics.PAYMENT_COMPLETED, event.getOrderId(), event);
    }
}
