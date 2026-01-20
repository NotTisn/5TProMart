package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.SuppliedProductDto;
import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.command.SupplierCreationCommand;
import com.fivetpromart.application.dto.command.SupplierUpdateCommand;
import com.fivetpromart.presentation.dto.request.SupplierRequest;
import com.fivetpromart.presentation.dto.response.SuppliedProductResponse;
import com.fivetpromart.presentation.dto.response.SupplierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierPresentationMapper {
    
    SupplierCreationCommand toCreateCommand(SupplierRequest request);
    
    SupplierUpdateCommand toUpdateCommand(SupplierRequest request);

    default SupplierResponse toResponse(SupplierDto supplier) {
        if (supplier == null) {
            return null;
        }
        
        SupplierResponse response = new SupplierResponse();
        response.setSupplierId(supplier.getSupplierId());
        response.setSupplierName(supplier.getSupplierName());
        response.setAddress(supplier.getAddress());
        response.setPhoneNumber(supplier.getPhoneNumber());
        response.setRepresentName(supplier.getRepresentName());
        response.setRepresentPhoneNumber(supplier.getRepresentPhoneNumber());
        response.setSupplierType(supplier.getSupplierType());
        response.setSuppliedProducts(mapSuppliedProductsToResponse(supplier.getSuppliedProducts()));
        response.setCurrentDebt(supplier.getCurrentDebt());
        
        return response;
    }
    
    default List<SuppliedProductResponse> mapSuppliedProductsToResponse(List<SuppliedProductDto> products) {
        if (products == null) {
            return null;
        }
        return products.stream()
                .map(this::mapSuppliedProductToResponse)
                .collect(Collectors.toList());
    }
    
    default SuppliedProductResponse mapSuppliedProductToResponse(SuppliedProductDto product) {
        if (product == null) {
            return null;
        }
        return SuppliedProductResponse.builder()
                .productId(product.getProductId())
                .lastImportPrice(product.getLastImportPrice())
                .lastImportDate(product.getLastImportDate())
                .build();
    }
}
