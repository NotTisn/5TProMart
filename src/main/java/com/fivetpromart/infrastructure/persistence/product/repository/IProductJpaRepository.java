package com.fivetpromart.infrastructure.persistence.product.repository;

import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IProductJpaRepository extends
        JpaRepository<ProductDbo,String>,
        JpaSpecificationExecutor<ProductDbo> {
    boolean existsByProductName(String productName);
}
