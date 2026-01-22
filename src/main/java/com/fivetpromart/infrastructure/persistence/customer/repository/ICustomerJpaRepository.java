package com.fivetpromart.infrastructure.persistence.customer.repository;

import com.fivetpromart.infrastructure.persistence.customer.CustomerDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ICustomerJpaRepository extends JpaRepository<CustomerDbo,String>, JpaSpecificationExecutor<CustomerDbo> {
    
    /**
     * Find customer by ID, only active
     */
    @Query("SELECT c FROM CustomerDbo c WHERE c.customerId = :customerId AND c.isActive = true")
    Optional<CustomerDbo> findByCustomerIdAndIsActiveTrue(@Param("customerId") String customerId);
    
    /**
     * Find customer by phone, only active
     */
    @Query("SELECT c FROM CustomerDbo c WHERE c.phoneNumber = :phoneNumber AND c.isActive = true")
    Optional<CustomerDbo> findByPhoneNumberAndIsActiveTrue(@Param("phoneNumber") String phoneNumber);
    
    Optional<CustomerDbo> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    
    /**
     * Find all active customers
     */
    @Query("SELECT c FROM CustomerDbo c WHERE c.isActive = true")
    List<CustomerDbo> findAllActive();
    
    /**
     * Search active customers
     */
    @Query("SELECT c FROM CustomerDbo c WHERE (LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR c.phoneNumber LIKE CONCAT('%', :keyword, '%')) AND c.isActive = true")
    List<CustomerDbo> searchActiveCustomers(@Param("keyword") String keyword);
    
    /**
     * Count new customers in date range (only active)
     */
    @Query("SELECT COUNT(c) FROM CustomerDbo c WHERE c.createdAt BETWEEN :startDate AND :endDate AND c.isActive = true")
    Integer countNewCustomers(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}
