package com.fivetpromart.infrastructure.persistence.customer.repository;

import com.fivetpromart.infrastructure.persistence.customer.CustomerDbo;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ICustomerJpaRepository extends JpaRepository<CustomerDbo,String>, JpaSpecificationExecutor<CustomerDbo> {
    Optional<CustomerDbo> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
