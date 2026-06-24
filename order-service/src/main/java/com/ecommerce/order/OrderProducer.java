package com.ecommerce.order;

import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

    /*
     * KafkaTemplate: Spring'in Kafka'ya mesaj göndermek için sağladığı yardımcı sınıf.
     * Eski yöntem: KafkaProducer'ı manuel oluştururdu, try-catch-finally ile kapatırdık.
     * Yeni yöntem: Spring inject ediyor, lifecycle yönetimini Spring hallediyor.
     */
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        /*
         * send(topic, key, value)
         * key   → orderId kullanıyoruz. Aynı key her zaman aynı partition'a gider.
         *         Bu sayede aynı siparişin event'leri sıralı işlenir.
         * value → OrderCreatedEvent nesnesi, JsonSerializer ile JSON'a çevrilerek gönderilir.
         *
         * Eski yöntem: send() bloklayan (synchronous) bir çağrıydı.
         * Yeni yöntem: CompletableFuture döner — async, thread'i bloklamaz.
         */
        CompletableFuture<SendResult<String, OrderCreatedEvent>> future =
                kafkaTemplate.send(KafkaTopics.ORDER_CREATED, event.getOrderId(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("The order event could not be sent. OrderId: {}, Error: {}",
                        event.getOrderId(), ex.getMessage());
            } else {
                log.info("Order event sent. OrderId: {}, Topic: {}, Partition: {}, Offset: {}",
                        event.getOrderId(),
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}
