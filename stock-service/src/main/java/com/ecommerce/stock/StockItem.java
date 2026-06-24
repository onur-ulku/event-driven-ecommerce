package com.ecommerce.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * Eski yöntem: javax.persistence kullanılırdı (Java EE).
 * Yeni yöntem: jakarta.persistence — Spring Boot 3.x ile Jakarta EE'ye geçildi.
 *
 * Gerçek projede bu entity PostgreSQL'deki stock_item tablosuna karşılık gelir.
 */
@Entity
@Getter
@NoArgsConstructor
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private int quantity;

    public StockItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public boolean hasEnoughStock(int requested) {
        return this.quantity >= requested;
    }

    public void decrease(int amount) {
        this.quantity -= amount;
    }
}
