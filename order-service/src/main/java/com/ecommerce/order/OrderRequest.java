package com.ecommerce.order;

import java.math.BigDecimal;

/*
 * REST endpoint'ten gelen sipariş isteğini taşıyan sınıf.
 *
 * Eski yöntem: class + getter/setter ile yazılırdı.
 * Yeni yöntem: Java 16+ record — immutable, getter otomatik, boilerplate yok.
 */
public record OrderRequest(
        String customerId,
        String productId,
        Integer quantity,
        BigDecimal price
) {}
