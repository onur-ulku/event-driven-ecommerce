package com.ecommerce.stock.init;

import com.ecommerce.stock.entity.StockItem;
import com.ecommerce.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
