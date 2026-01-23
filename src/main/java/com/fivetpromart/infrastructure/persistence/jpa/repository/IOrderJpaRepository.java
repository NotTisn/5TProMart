package com.fivetpromart.infrastructure.persistence.jpa.repository;

import com.fivetpromart.infrastructure.persistence.jpa.entity.OrderDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface IOrderJpaRepository extends JpaRepository<OrderDbo, String>, JpaSpecificationExecutor<OrderDbo> {
    
    @Query("SELECT SUM(o.totalAmount) FROM OrderDbo o WHERE o.status IN ('PAID', 'COMPLETED') AND o.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM OrderDbo o WHERE o.status IN ('PAID', 'COMPLETED') AND o.orderDate BETWEEN :startDate AND :endDate")
    Integer countCompletedOrders(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(DISTINCT o.customerId) FROM OrderDbo o WHERE o.status IN ('PAID', 'COMPLETED') AND o.orderDate BETWEEN :startDate AND :endDate")
    Integer countUniqueCustomers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
