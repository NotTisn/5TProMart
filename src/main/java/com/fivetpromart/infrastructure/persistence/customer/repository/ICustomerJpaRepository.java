package com.fivetpromart.infrastructure.persistence.customer.repository;

import com.fivetpromart.infrastructure.persistence.customer.CustomerDbo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICustomerJpaRepository extends JpaRepository<CustomerDbo,String> {
    Optional<CustomerDbo> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
