package com.fivetpromart.infrastructure.persistence.supplier.repository;

import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISupplierJpaRepository extends JpaRepository<SupplierDbo, String> {
    boolean existsById(String supplierId);
}
