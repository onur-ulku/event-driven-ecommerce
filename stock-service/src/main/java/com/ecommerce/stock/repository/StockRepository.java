package com.ecommerce.stock.repository;

import com.ecommerce.stock.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockItem, Long> {

    Optional<StockItem> findByProductId(String productId);
}
