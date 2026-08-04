package com.ecommerce.stock.service;

import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.stock.dto.StockResponse;

public interface StockService {

    void reserve(OrderCreatedEvent event);

    void compensate(String productId, int quantity);

    StockResponse getStock(String productId);
}
