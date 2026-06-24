package com.ecommerce.payment;

import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public void sendPaymentCompletedEvent(PaymentCompletedEvent event) {
        kafkaTemplate.send(KafkaTopics.PAYMENT_COMPLETED, event.getOrderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send payment event. OrderId: {}, Error: {}",
                                event.getOrderId(), ex.getMessage());
                    } else {
                        log.info("Payment event sent. OrderId: {}, Success: {}, Partition: {}, Offset: {}",
                                event.getOrderId(),
                                event.isSuccess(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
