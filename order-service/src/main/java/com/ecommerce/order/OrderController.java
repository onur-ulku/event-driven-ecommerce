package com.ecommerce.order;

import com.ecommerce.common.event.OrderCreatedEvent;
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

    private final OrderProducer orderProducer;

    /*
     * POST /orders
     * Sipariş isteği gelir → OrderCreatedEvent oluşturulur → Kafka'ya gönderilir.
     *
     * Eski yöntem (senkron/REST): Controller → Stock Service'i REST ile çağırır,
     *   cevap bekler, sonra Payment Service'i çağırır. Bir servis yavaşlarsa hepsi yavaşlar.
     *
     * Yeni yöntem (event-driven): Controller sadece Kafka'ya event gönderir ve döner.
     *   Stock, Payment, Notification servisleri kendi hızlarında işler. Birbirinden bağımsız.
     */
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        log.info("New order received. Customer: {}, Product: {}", request.customerId(), request.productId());

        OrderCreatedEvent event = new OrderCreatedEvent(
                request.customerId(),
                request.productId(),
                request.quantity(),
                request.price()
        );

        orderProducer.sendOrderCreatedEvent(event);

        return ResponseEntity.ok("Order received. OrderId:" + event.getOrderId());
    }
}
