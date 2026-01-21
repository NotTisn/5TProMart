package com.fivetpromart.infrastructure.persistence.customer.repository;

import com.fivetpromart.infrastructure.persistence.customer.CustomerDbo;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ICustomerJpaRepository extends JpaRepository<CustomerDbo,String>, JpaSpecificationExecutor<CustomerDbo> {
    Optional<CustomerDbo> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    
    @Query("SELECT COUNT(c) FROM CustomerDbo c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    Integer countNewCustomers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
