package com.fivetpromart.infrastructure.persistence.product.repository;

import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProductJpaRepository extends
        JpaRepository<ProductDbo,String>,
        JpaSpecificationExecutor<ProductDbo> {
    
    // Check existence including soft-deleted
    boolean existsByProductName(String productName);

    /**
     * Find product by ID, only active products
     */
    @Query("SELECT p FROM ProductDbo p WHERE p.productId = :productId AND p.isActive = true")
    Optional<ProductDbo> findByProductIdAndIsActiveTrue(@Param("productId") String productId);

    /**
     * Find product by name (case-insensitive), only active
     */
    @Query("SELECT p FROM ProductDbo p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isActive = true")
    List<ProductDbo> findByProductNameContainingAndIsActiveTrue(@Param("name") String name);

    /**
     * Find all active products
     */
    @Query("SELECT p FROM ProductDbo p WHERE p.isActive = true")
    List<ProductDbo> findAllActive();

    @Query("SELECT COUNT(p) FROM ProductDbo p WHERE p.isActive = true")
    Long countAllProducts();

    @Query("SELECT COUNT(p) FROM ProductDbo p WHERE p.isActive = true AND p.totalStockQuantity > :threshold")
    Long countByTotalStockQuantityGreaterThan(Long threshold);

    @Query("SELECT COUNT(p) FROM ProductDbo p WHERE p.isActive = true AND p.totalStockQuantity = :quantity")
    Long countByTotalStockQuantityEquals(Long quantity);

    @Query("SELECT COALESCE(SUM(s.stockQuantity), 0) FROM StockInventoryDbo s WHERE s.productId = :productId")
    Integer calculateTotalStockQuantity(@Param("productId") String productId);
}
