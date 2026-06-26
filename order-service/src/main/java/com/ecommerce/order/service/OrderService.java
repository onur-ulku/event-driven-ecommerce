package com.ecommerce.order.service;

import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderRequest;

public interface OrderService {

    OrderCreatedEvent createOrder(OrderRequest request);
}
