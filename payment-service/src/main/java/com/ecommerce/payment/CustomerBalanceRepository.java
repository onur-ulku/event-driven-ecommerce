package com.ecommerce.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerBalanceRepository extends JpaRepository<CustomerBalance, Long> {

    Optional<CustomerBalance> findByCustomerId(String customerId);
}
