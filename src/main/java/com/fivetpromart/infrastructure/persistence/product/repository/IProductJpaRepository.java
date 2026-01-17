package com.fivetpromart.infrastructure.persistence.product.repository;

import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IProductJpaRepository extends
        JpaRepository<ProductDbo,String>,
        JpaSpecificationExecutor<ProductDbo> {
    boolean existsByProductName(String productName);

    @Query("SELECT COUNT(p) FROM ProductDbo p")
    Long countAllProducts();

    @Query("SELECT COUNT(p) FROM ProductDbo p WHERE p.totalStockQuantity > :threshold")
    Long countByTotalStockQuantityGreaterThan(Long threshold);

    @Query("SELECT COUNT(p) FROM ProductDbo p WHERE p.totalStockQuantity = :quantity")
    Long countByTotalStockQuantityEquals(Long quantity);
}
