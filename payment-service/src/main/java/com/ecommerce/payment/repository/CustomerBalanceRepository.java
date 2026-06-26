package com.ecommerce.payment.repository;

import com.ecommerce.payment.entity.CustomerBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerBalanceRepository extends JpaRepository<CustomerBalance, Long> {

    Optional<CustomerBalance> findByCustomerId(String customerId);
}
