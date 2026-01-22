package com.fivetpromart.infrastructure.persistence.supplier.repository;

import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ISupplierJpaRepository extends
        JpaRepository<SupplierDbo, String>,
        JpaSpecificationExecutor<SupplierDbo> {
    
    boolean existsById(String supplierId);

    /**
     * Find supplier by ID, only active
     */
    @Query("SELECT s FROM SupplierDbo s WHERE s.supplierId = :supplierId AND s.isActive = true")
    Optional<SupplierDbo> findBySupplierIdAndIsActiveTrue(@Param("supplierId") String supplierId);

    /**
     * Find all active suppliers
     */
    @Query("SELECT s FROM SupplierDbo s WHERE s.isActive = true")
    List<SupplierDbo> findAllActive();

    /**
     * Search active suppliers by name
     */
    @Query("SELECT s FROM SupplierDbo s WHERE LOWER(s.supplierName) LIKE LOWER(CONCAT('%', :name, '%')) AND s.isActive = true")
    List<SupplierDbo> searchActiveSuppliers(@Param("name") String name);

    /**
     * Count active suppliers
     */
    @Query("SELECT COUNT(s) FROM SupplierDbo s WHERE s.isActive = true")
    Long countActiveSuppliers();
}
