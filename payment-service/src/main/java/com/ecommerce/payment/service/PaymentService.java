package com.ecommerce.payment.service;

import com.ecommerce.common.event.StockReservedEvent;

import java.math.BigDecimal;

public interface PaymentService {

    void processPayment(StockReservedEvent event, BigDecimal amount);
}
