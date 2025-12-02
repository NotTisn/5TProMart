package com.fivetpromart.infrastructure.persistence.supplier.adapter;

import com.fivetpromart.application.port.out.ISupplierRepository;
import com.fivetpromart.domain.model.Supplier;
import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import com.fivetpromart.infrastructure.persistence.supplier.mapper.SupplierPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.supplier.repository.ISupplierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SupplierAdapter implements ISupplierRepository {

    private final ISupplierJpaRepository supplierRepository;
    private final SupplierPersistenceMapper mapper;

    @Override
    public Supplier save(Supplier supplier) {
        SupplierDbo dbo = mapper.toDbo(supplier);
        SupplierDbo savedDbo = supplierRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public Supplier findById(String supplierId) {
        return null;
    }

    @Override
    public List<Supplier> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(String supplierId) {

    }

    @Override
    public boolean existsById(String supplierId) {
        return false;
    }
}
