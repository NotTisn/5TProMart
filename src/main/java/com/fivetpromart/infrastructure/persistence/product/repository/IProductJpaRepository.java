package com.fivetpromart.infrastructure.persistence.product.repository;

import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IProductJpaRepository extends
        JpaRepository<ProductDbo,String>,
        JpaSpecificationExecutor<ProductDbo> {
    boolean existsByProductName(String productName);
    
    @Query("SELECT COALESCE(SUM(s.stockQuantity), 0) FROM StockInventoryDbo s WHERE s.productId = :productId")
    Integer calculateTotalStockQuantity(@Param("productId") String productId);
}
