package com.ecommerce.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class CustomerBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;
    private BigDecimal balance;

    public CustomerBalance(String customerId, BigDecimal balance) {
        this.customerId = customerId;
        this.balance = balance;
    }

    public boolean hasSufficientBalance(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    public void deduct(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}
