package com.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/*
 * Stock Service, OrderCreatedEvent'i işledikten sonra bu event'i Kafka'ya gönderir.
 * Payment Service bu event'i dinleyerek ödeme işlemini başlatır.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReservedEvent {

    private String orderId;
    private String customerId;
    private String productId;
    private Integer quantity;
    private BigDecimal amount;
    private boolean success;
    private String failureReason;
    private String reservedAt;
}
