package com.ecommerce.payment;

import com.ecommerce.common.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CustomerBalanceRepository customerBalanceRepository;

    @Transactional
    public PaymentResult processPayment(StockReservedEvent event, BigDecimal amount) {
        if (!event.isSuccess()) {
            log.warn("Stock reservation failed, skipping payment. OrderId: {}", event.getOrderId());
            return PaymentResult.failure("Stock reservation failed");
        }

        CustomerBalance customerBalance = customerBalanceRepository
                .findByCustomerId(event.getCustomerId())
                .orElse(null);

        if (customerBalance == null) {
            log.warn("Customer not found. CustomerId: {}", event.getCustomerId());
            return PaymentResult.failure("Customer not found");
        }

        if (!customerBalance.hasSufficientBalance(amount)) {
            log.warn("Insufficient balance. OrderId: {}, CustomerId: {}", event.getOrderId(), event.getCustomerId());
            return PaymentResult.failure("Insufficient balance");
        }

        customerBalance.deduct(amount);
        customerBalanceRepository.save(customerBalance);

        log.info("Payment successful. OrderId: {}, CustomerId: {}, Amount: {}",
                event.getOrderId(), event.getCustomerId(), amount);
        return PaymentResult.success();
    }
}
