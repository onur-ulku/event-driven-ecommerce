package com.ecommerce.stock.consumer;

import com.ecommerce.common.kafka.KafkaGroups;
import com.ecommerce.common.event.PaymentCompletedEvent;
import com.ecommerce.common.kafka.KafkaTopics;
import com.ecommerce.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentResultConsumer {

    private static final String STOCK_LOCK_PREFIX = "lock:stock:";

    private final StockService stockService;
    private final RedissonClient redissonClient;

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = KafkaGroups.STOCK_SERVICE)
    public void handlePaymentCompleted(
            @Payload PaymentCompletedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Payment result received. OrderId: {}, Success: {}, Partition: {}, Offset: {}",
                event.getOrderId(), event.isSuccess(), partition, offset);

        if (event.isSuccess()) {
            return;
        }

        log.warn("Payment failed, compensating stock. OrderId: {}, ProductId: {}, Quantity: {}",
                event.getOrderId(), event.getProductId(), event.getQuantity());

        RLock lock = redissonClient.getLock(STOCK_LOCK_PREFIX + event.getProductId());
        boolean locked = false;
        try {
            locked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("Could not acquire lock, skipping compensation. ProductId: {}", event.getProductId());
                return;
            }
            stockService.compensate(event.getProductId(), event.getQuantity());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while acquiring stock lock", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
