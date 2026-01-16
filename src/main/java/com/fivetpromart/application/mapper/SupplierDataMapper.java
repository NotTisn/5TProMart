package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.SuppliedProductDto;
import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.domain.model.Supplier;
import com.fivetpromart.domain.model.SuppliedProduct;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SupplierDataMapper {
    
    default SupplierDto toDto(Supplier domain) {
        if (domain == null) {
            return null;
        }
        
        return SupplierDto.builder()
                .supplierId(domain.getSupplierId())
                .supplierName(domain.getSupplierName())
                .address(domain.getAddress())
                .phoneNumber(domain.getPhoneNumber())
                .representName(domain.getRepresentName())
                .representPhoneNumber(domain.getRepresentPhoneNumber())
                .supplierType(domain.getSupplierType())
                .suppliedProducts(mapSuppliedProducts(domain.getSuppliedProducts()))
                .currentDebt(domain.getCurrentDebt())
                .build();
    }
    
    default List<SuppliedProductDto> mapSuppliedProducts(List<SuppliedProduct> products) {
        if (products == null) {
            return null;
        }
        return products.stream()
                .map(this::mapSuppliedProduct)
                .collect(Collectors.toList());
    }
    
    default SuppliedProductDto mapSuppliedProduct(SuppliedProduct product) {
        if (product == null) {
            return null;
        }
        return SuppliedProductDto.builder()
                .productId(product.getProductId())
                .lastImportPrice(product.getLastImportPrice())
                .lastImportDate(product.getLastImportDate())
                .build();
    }
}
