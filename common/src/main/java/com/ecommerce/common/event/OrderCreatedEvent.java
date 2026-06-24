package com.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/*
 * Order Service bu event'i Kafka'ya gönderir.
 * Stock Service, Payment Service ve Notification Service bu event'i dinler.
 * Servisler bu event'i Kafka'dan okur — birbirini bilmez, gevşek bağımlılık.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private String orderId;
    private String customerId;
    private String productId;
    private Integer quantity;
    private BigDecimal price;
    private String createdAt;

    public OrderCreatedEvent(String customerId, String productId, Integer quantity, BigDecimal price) {
        this.orderId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = Instant.now().toString(); // → "2026-06-24T13:24:58.055Z"
    }
}
