package com.fivetpromart.infrastructure.persistence.supplier.mapper;

import com.fivetpromart.domain.model.Supplier;
import com.fivetpromart.domain.model.SuppliedProduct;
import com.fivetpromart.infrastructure.persistence.supplier.SuppliedProductDbo;
import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SupplierPersistenceMapper {
    
    default SupplierDbo toDbo(Supplier domain) {
        if (domain == null) return null;

        SupplierDbo dbo = SupplierDbo.builder()
                .supplierId(domain.getSupplierId())
                .supplierName(domain.getSupplierName())
                .address(domain.getAddress())
                .phoneNumber(domain.getPhoneNumber())
                .representName(domain.getRepresentName())
                .representPhoneNumber(domain.getRepresentPhoneNumber())
                .supplierType(domain.getSupplierType())
                .currentDebt(domain.getCurrentDebt())
                .build();
        
        // Map supplied products
        if (domain.getSuppliedProducts() != null) {
            List<SuppliedProductDbo> productDbos = domain.getSuppliedProducts().stream()
                    .map(p -> mapSuppliedProductToDbo(p, domain.getSupplierId()))
                    .collect(Collectors.toList());
            dbo.setSuppliedProducts(productDbos);
        }
        
        return dbo;
    }

    default Supplier toDomain(SupplierDbo dbo) {
        if (dbo == null) return null;

        List<SuppliedProduct> suppliedProducts = new ArrayList<>();
        if (dbo.getSuppliedProducts() != null) {
            suppliedProducts = dbo.getSuppliedProducts().stream()
                    .map(this::mapSuppliedProductToDomain)
                    .collect(Collectors.toList());
        }

        return Supplier.reconstitute(
                dbo.getSupplierId(),
                dbo.getSupplierName(),
                dbo.getAddress(),
                dbo.getPhoneNumber(),
                dbo.getRepresentName(),
                dbo.getRepresentPhoneNumber(),
                dbo.getSupplierType(),
                suppliedProducts,
                dbo.getCurrentDebt()
        );
    }
    
    default SuppliedProductDbo mapSuppliedProductToDbo(SuppliedProduct domain, String supplierId) {
        if (domain == null) return null;
        
        return SuppliedProductDbo.builder()
                .supplierId(supplierId)
                .productId(domain.getProductId())
                .lastImportPrice(domain.getLastImportPrice())
                .lastImportDate(domain.getLastImportDate())
                .build();
    }
    
    default SuppliedProduct mapSuppliedProductToDomain(SuppliedProductDbo dbo) {
        if (dbo == null) return null;
        
        return SuppliedProduct.builder()
                .productId(dbo.getProductId())
                .lastImportPrice(dbo.getLastImportPrice())
                .lastImportDate(dbo.getLastImportDate())
                .build();
    }
}
