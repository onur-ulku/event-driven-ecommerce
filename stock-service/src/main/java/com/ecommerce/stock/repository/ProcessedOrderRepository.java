package com.ecommerce.stock.repository;

import com.ecommerce.stock.entity.ProcessedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedOrderRepository extends JpaRepository<ProcessedOrder, String> {
}
