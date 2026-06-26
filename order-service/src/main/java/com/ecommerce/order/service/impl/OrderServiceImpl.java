package com.ecommerce.order.service.impl;

import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.producer.OrderProducer;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderProducer orderProducer;

    @Override
    public OrderCreatedEvent createOrder(OrderRequest request) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                request.customerId(),
                request.productId(),
                request.quantity(),
                request.price()
        );
        orderProducer.sendOrderCreatedEvent(event);
        return event;
    }
}
