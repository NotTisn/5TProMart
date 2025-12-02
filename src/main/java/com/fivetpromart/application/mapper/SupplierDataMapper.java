package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.domain.model.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierDataMapper {
    SupplierDto toDto(Supplier domain);
}
