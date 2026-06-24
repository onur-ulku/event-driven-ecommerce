package com.ecommerce.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public boolean reserve(String productId, int quantity) {
        Optional<StockItem> optional = stockRepository.findByProductId(productId);

        if (optional.isEmpty()) {
            log.warn("Product not found. ProductId: {}", productId);
            return false;
        }

        StockItem item = optional.get();

        if (!item.hasEnoughStock(quantity)) {
            log.warn("Insufficient stock. ProductId: {}, Available: {}, Requested: {}",
                    productId, item.getQuantity(), quantity);
            return false;
        }

        item.decrease(quantity);
        stockRepository.save(item);
        log.info("Stock reserved. ProductId: {}, Remaining: {}", productId, item.getQuantity());
        return true;
    }
}
