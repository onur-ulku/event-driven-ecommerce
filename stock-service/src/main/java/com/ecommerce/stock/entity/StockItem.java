package com.ecommerce.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
