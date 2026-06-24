package com.ecommerce.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*
 * Loads initial stock data on application startup.
 * Old approach: data.sql or import.sql files.
 * New approach: CommandLineRunner — runs after Spring context is fully initialized,
 *               giving full access to repositories and beans.
 *
 * In a real project this class would not exist — data would already be in the database.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StockRepository stockRepository;

    @Override
    public void run(String... args) {
        saveIfNotExists("1001", 100);
        saveIfNotExists("2002", 200);
        saveIfNotExists("3003", 300);
        saveIfNotExists("4004", 400);
        log.info("Initial stock data loaded.");
    }

    private void saveIfNotExists(String productId, int quantity) {
        if (stockRepository.findByProductId(productId).isEmpty()) {
            stockRepository.save(new StockItem(productId, quantity));
            log.info("Stock added. ProductId: {}, Quantity: {}", productId, quantity);
        } else {
            log.info("Stock already exists. ProductId: {}", productId);
        }
    }
}
