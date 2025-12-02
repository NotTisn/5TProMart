package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.command.SupplierCreationCommand;
import com.fivetpromart.application.dto.command.SupplierUpdateCommand;
import com.fivetpromart.domain.model.Supplier;
import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import com.fivetpromart.presentation.dto.request.SupplierRequest;
import com.fivetpromart.presentation.dto.response.SupplierResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierPresentationMapper {
    SupplierCreationCommand toCreateCommand (SupplierRequest request);
    SupplierUpdateCommand toUpdateCommand (SupplierRequest request);

    SupplierResponse toResponse (SupplierDto supplier);
}
