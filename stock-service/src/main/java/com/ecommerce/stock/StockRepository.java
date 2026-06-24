package com.ecommerce.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Eski yöntem: JDBC ile manuel SQL yazılırdı — ResultSet, PreparedStatement vs.
 * Yeni yöntem: JpaRepository — CRUD işlemleri hazır gelir, SQL yazmana gerek yok.
 *
 * JpaRepository<StockItem, Long>:
 * StockItem → hangi entity
 * Long      → primary key tipi
 */
public interface StockRepository extends JpaRepository<StockItem, Long> {

    Optional<StockItem> findByProductId(String productId);
}
