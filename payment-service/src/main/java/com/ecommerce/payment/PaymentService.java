package com.ecommerce.payment;

import com.ecommerce.common.event.KafkaTopics;
import com.ecommerce.common.event.PaymentCompletedEvent;
import com.ecommerce.common.event.StockReservedEvent;
import com.ecommerce.common.outbox.OutboxEvent;
import com.ecommerce.common.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CustomerBalanceRepository customerBalanceRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processPayment(StockReservedEvent event, BigDecimal amount) {
        PaymentCompletedEvent paymentEvent;

        if (!event.isSuccess()) {
            log.warn("Stock reservation failed, skipping payment. OrderId: {}", event.getOrderId());
            paymentEvent = buildPaymentEvent(event, false, "Stock reservation failed");
        } else {
            CustomerBalance customerBalance = customerBalanceRepository
                    .findByCustomerId(event.getCustomerId())
                    .orElse(null);

            if (customerBalance == null) {
                log.warn("Customer not found. CustomerId: {}", event.getCustomerId());
                paymentEvent = buildPaymentEvent(event, false, "Customer not found");
            } else if (!customerBalance.hasSufficientBalance(amount)) {
                log.warn("Insufficient balance. OrderId: {}, CustomerId: {}", event.getOrderId(), event.getCustomerId());
                paymentEvent = buildPaymentEvent(event, false, "Insufficient balance");
            } else {
                customerBalance.deduct(amount);
                customerBalanceRepository.save(customerBalance);
                log.info("Payment successful. OrderId: {}, CustomerId: {}, Amount: {}",
                        event.getOrderId(), event.getCustomerId(), amount);
                paymentEvent = buildPaymentEvent(event, true, null);
            }
        }

        saveToOutbox(paymentEvent);
    }

    private PaymentCompletedEvent buildPaymentEvent(StockReservedEvent event, boolean success, String failureReason) {
        return new PaymentCompletedEvent(
                event.getOrderId(),
                event.getCustomerId(),
                event.getAmount(),
                success,
                failureReason,
                Instant.now().toString()
        );
    }

    private void saveToOutbox(PaymentCompletedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            outboxRepository.save(new OutboxEvent(
                    KafkaTopics.PAYMENT_COMPLETED,
                    event.getOrderId(),
                    payload,
                    LocalDateTime.now().toString()
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payment event", e);
        }
    }
}
