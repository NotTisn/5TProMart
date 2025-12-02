package com.fivetpromart.infrastructure.persistence.supplier.mapper;

import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.domain.model.Supplier;
import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierPersistenceMapper {
    default SupplierDbo toDbo(Supplier domain){
        if(domain == null) return null;

        return SupplierDbo.builder()
                .supplierId(domain.getSupplierId())
                .supplierName(domain.getSupplierName())
                .supplierType(domain.getSupplierType())
                .address(domain.getAddress())
                .phoneNumber(domain.getPhoneNumber())
                .suppliedProductType(domain.getSuppliedProductType())
                .currentDebt(domain.getCurrentDebt())
                .build();
    }

    default Supplier toDomain(SupplierDbo dbo){
        if(dbo == null) return null;

        return Supplier.reconstitute(
                dbo.getSupplierId(),
                dbo.getSupplierName(),
                dbo.getSupplierType(),
                dbo.getPhoneNumber(),
                dbo.getAddress(),
                dbo.getSuppliedProductType(),
                dbo.getCurrentDebt()
        );
    }
}
