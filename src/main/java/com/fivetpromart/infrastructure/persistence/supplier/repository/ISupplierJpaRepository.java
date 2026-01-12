package com.fivetpromart.infrastructure.persistence.supplier.repository;

import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISupplierJpaRepository extends
        JpaRepository<SupplierDbo, String>,
        JpaSpecificationExecutor<SupplierDbo> {
    boolean existsById(String supplierId);

}
