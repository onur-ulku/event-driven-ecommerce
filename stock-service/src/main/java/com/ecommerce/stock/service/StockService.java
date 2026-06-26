package com.ecommerce.stock.service;

import com.ecommerce.common.event.OrderCreatedEvent;

public interface StockService {

    void reserve(OrderCreatedEvent event);

    void compensate(String productId, int quantity);
}
