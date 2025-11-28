package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.domain.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerDataMapper {
    CustomerDto toDto(Customer domain);
}