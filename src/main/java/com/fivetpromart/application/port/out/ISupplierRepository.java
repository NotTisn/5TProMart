package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.query.SupplierSearchQuery;
import com.fivetpromart.domain.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ISupplierRepository {
    Supplier save(Supplier supplier);
    Optional<Supplier> findById(String supplierId);
    Optional<Supplier> findByIdIncludingDeleted(String supplierId);
    List<Supplier> findAll();
    void deleteById(String supplierId);
    boolean existsById(String supplierId);
    Page<Supplier> searchSuppliers(SupplierSearchQuery query, Pageable pageable);
}
