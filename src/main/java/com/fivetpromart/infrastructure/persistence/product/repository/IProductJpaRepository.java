package com.fivetpromart.infrastructure.persistence.product.repository;

import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductJpaRepository extends JpaRepository<ProductDbo,String> {
    boolean existsByProductName(String productName);
}
