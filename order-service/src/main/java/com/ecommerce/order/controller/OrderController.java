package com.ecommerce.order.controller;

import com.ecommerce.common.event.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        log.info("New order received. Customer: {}, Product: {}", request.customerId(), request.productId());
        OrderCreatedEvent event = orderService.createOrder(request);
        return ResponseEntity.ok("Order received. OrderId:" + event.getOrderId());
    }
}
