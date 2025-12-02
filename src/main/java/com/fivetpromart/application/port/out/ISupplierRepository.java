package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.Supplier;

import java.util.List;

public interface ISupplierRepository {
    Supplier save(Supplier supplier);
    Supplier findById(String supplierId);
    List<Supplier> findAll();
    void deleteById(String supplierId);
    boolean existsById(String supplierId);
}
