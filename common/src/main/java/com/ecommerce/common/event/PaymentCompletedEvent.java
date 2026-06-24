package com.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/*
 * Payment Service, StockReservedEvent'i işledikten sonra bu event'i Kafka'ya gönderir.
 * Notification Service bu event'i dinleyerek kullanıcıya bildirim gönderir.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {

    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private boolean success;
    private String failureReason;
    private String completedAt;
}
