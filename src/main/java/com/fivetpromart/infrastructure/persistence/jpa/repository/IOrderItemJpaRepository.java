package com.fivetpromart.infrastructure.persistence.jpa.repository;

import com.fivetpromart.infrastructure.persistence.jpa.entity.OrderItemDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IOrderItemJpaRepository extends JpaRepository<OrderItemDbo, String> {

    @Query("""
            SELECT SUM(oi.quantity * si.importPrice)
            FROM OrderItemDbo oi
            JOIN StockInventoryDbo si ON oi.lotId = si.lotId
            JOIN OrderDbo o ON oi.order.orderId = o.orderId
            WHERE o.status = 'COMPLETED' AND o.orderDate BETWEEN :startDate AND :endDate
            """)
    BigDecimal calculateCostOfGoodsSold(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT SUM(oi.quantity)
            FROM OrderItemDbo oi
            JOIN OrderDbo o ON oi.order.orderId = o.orderId
            WHERE o.status = 'COMPLETED' AND o.orderDate BETWEEN :startDate AND :endDate
            """)
    Integer sumQuantitySold(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT c.categoryId, c.categoryName, SUM(oi.subTotal), SUM(oi.quantity), COUNT(DISTINCT o.orderId)
            FROM OrderItemDbo oi
            JOIN OrderDbo o ON oi.order.orderId = o.orderId
            JOIN ProductDbo p ON oi.productId = p.productId
            JOIN CategoryDbo c ON p.categoryId = c.categoryId
            WHERE o.status = 'COMPLETED' AND o.orderDate BETWEEN :startDate AND :endDate
            GROUP BY c.categoryId, c.categoryName
            """)
    List<Object[]> getCategoryRevenue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT p.productId, p.productName, c.categoryName, SUM(oi.subTotal), SUM(oi.quantity),
            COALESCE((SELECT SUM(si.stockQuantity) FROM StockInventoryDbo si WHERE si.productId = p.productId), 0)
            FROM OrderItemDbo oi
            JOIN OrderDbo o ON oi.order.orderId = o.orderId
            JOIN ProductDbo p ON oi.productId = p.productId
            JOIN CategoryDbo c ON p.categoryId = c.categoryId
            WHERE o.status = 'COMPLETED' AND o.orderDate BETWEEN :startDate AND :endDate
            GROUP BY p.productId, p.productName, c.categoryName
            """)
    List<Object[]> getTopSellingProducts(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
