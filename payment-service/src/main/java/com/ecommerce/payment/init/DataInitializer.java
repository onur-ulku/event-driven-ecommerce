package com.ecommerce.payment.init;

import com.ecommerce.payment.entity.CustomerBalance;
import com.ecommerce.payment.repository.CustomerBalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CustomerBalanceRepository customerBalanceRepository;

    @Override
    public void run(String... args) {
        saveIfNotExists("1991", new BigDecimal("1000.00"));
        saveIfNotExists("2992", new BigDecimal("50.00"));
        saveIfNotExists("3993", new BigDecimal("500.00"));
        saveIfNotExists("4994", new BigDecimal("600.00"));
        log.info("Customer balances loaded.");
    }

    private void saveIfNotExists(String customerId, BigDecimal balance) {
        customerBalanceRepository.findByCustomerId(customerId)
                .orElseGet(() -> customerBalanceRepository.save(new CustomerBalance(customerId, balance)));
    }
}
