package com.ecommerce.payment.gateway;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Dış ödeme ağ geçidini (banka API'si gibi) simüle eder.
 * @CircuitBreaker: bu çağrı sürekli hata verirse devre AÇILIR; sonraki çağrılar
 * gateway'e hiç gitmeden fallback'e düşer (fail-fast). "paymentGateway" ismi
 * application.yml'deki resilience4j ayarlarıyla eşleşir.
 */
@Slf4j
@Component
public class PaymentGatewayClient {

    // Simülasyon: dış servisin hata verme olasılığı (0.0–1.0). yaml'dan ayarlanır.
    @Value("${payment.gateway.failure-rate:0.0}")
    private double failureRate;

    @CircuitBreaker(name = "paymentGateway", fallbackMethod = "chargeFallback")
    public boolean charge(String customerId, BigDecimal amount) {
        if (ThreadLocalRandom.current().nextDouble() < failureRate) {  //nextDouble: [0.0, 1.0)
            log.error("[GATEWAY] External payment gateway error. CustomerId: {}", customerId);
            throw new PaymentGatewayException("Payment gateway unavailable");
        }
        log.info("[GATEWAY] Charge successful. CustomerId: {}, Amount: {}", customerId, amount);
        return true;
    }

    private boolean chargeFallback(String customerId, BigDecimal amount, Throwable t) {
        log.warn("[GATEWAY] Fallback triggered (circuit open or error). CustomerId: {}, Reason: {}",
                customerId, t.getMessage());
        return false;
    }
}
