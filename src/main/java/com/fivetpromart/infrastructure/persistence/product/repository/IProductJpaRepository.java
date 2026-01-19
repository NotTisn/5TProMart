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
    boolean existsByProductName(String productName);

    /**
     * Find product by ID, excluding soft-deleted products
     */
    @Query("SELECT p FROM ProductDbo p WHERE p.productId = :productId AND p.deletedAt IS NULL")
    Optional<ProductDbo> findByIdAndNotDeleted(@Param("productId") String productId);

    /**
     * Find product by name (case-insensitive), excluding soft-deleted
     */
    @Query("SELECT p FROM ProductDbo p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%')) AND p.deletedAt IS NULL")
    List<ProductDbo> findByProductNameContainingAndNotDeleted(@Param("name") String name);

    /**
     * Find all products, excluding soft-deleted
     */
    @Query("SELECT p FROM ProductDbo p WHERE p.deletedAt IS NULL")
    List<ProductDbo> findAllNotDeleted();

    @Query("SELECT COUNT(p) FROM ProductDbo p WHERE p.deletedAt IS NULL")
    Long countAllProducts();

    @Query("SELECT COUNT(p) FROM ProductDbo p WHERE p.deletedAt IS NULL AND p.totalStockQuantity > :threshold")
    Long countByTotalStockQuantityGreaterThan(Long threshold);

    @Query("SELECT COUNT(p) FROM ProductDbo p WHERE p.deletedAt IS NULL AND p.totalStockQuantity = :quantity")
    Long countByTotalStockQuantityEquals(Long quantity);

    @Query("SELECT COALESCE(SUM(s.stockQuantity), 0) FROM StockInventoryDbo s WHERE s.productId = :productId")
    Integer calculateTotalStockQuantity(@Param("productId") String productId);
}
