package com.ecommerce.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProcessedOrder {

    @Id
    private String orderId;

    private String processedAt;

    public ProcessedOrder(String orderId, String processedAt) {
        this.orderId = orderId;
        this.processedAt = processedAt;
    }
}
