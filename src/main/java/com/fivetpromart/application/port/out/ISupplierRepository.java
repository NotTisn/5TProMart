package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface ISupplierRepository {
    Supplier save(Supplier supplier);
    Optional<Supplier> findById(String supplierId);
    List<Supplier> findAll();
    void deleteById(String supplierId);
    boolean existsById(String supplierId);
}
