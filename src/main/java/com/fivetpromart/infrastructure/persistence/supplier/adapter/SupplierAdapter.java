package com.fivetpromart.infrastructure.persistence.supplier.adapter;

import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.query.SupplierSearchQuery;
import com.fivetpromart.application.port.out.ISupplierRepository;
import com.fivetpromart.domain.model.Supplier;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import com.fivetpromart.infrastructure.persistence.product.spec.ProductSpecification;
import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import com.fivetpromart.infrastructure.persistence.supplier.mapper.SupplierPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.supplier.repository.ISupplierJpaRepository;
import com.fivetpromart.infrastructure.persistence.supplier.spec.SupplierSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<Supplier> findById(String supplierId) {
        return supplierRepository.findById(supplierId).map(mapper::toDomain);
    }

    @Override
    public List<Supplier> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(String supplierId) {
        supplierRepository.deleteById(supplierId);
    }

    @Override
    public boolean existsById(String supplierId) {
        return supplierRepository.existsById(supplierId);
    }

    @Override
    public Page<Supplier> searchSuppliers(SupplierSearchQuery query, Pageable pageable) {
        // 1. Tạo Specification từ DTO Filter
        Specification<SupplierDbo> spec = SupplierSpecification.getSpec(query);

        // 2. Truyền thẳng Pageable vào JPA
        // JPA tự động xử lý LIMIT, OFFSET, ORDER BY dựa trên Pageable
        Page<SupplierDbo> dboPage = supplierRepository.findAll(spec, pageable);

        return dboPage.map(mapper::toDomain);
    }
}
