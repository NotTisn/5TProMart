package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCommand;
import com.fivetpromart.domain.model.Customer;
import com.fivetpromart.presentation.dto.request.CustomerInitRequest;
import com.fivetpromart.presentation.dto.response.CustomerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerPresentationMapper {
    CustomerCommand toDto(CustomerInitRequest domain);
    CustomerResponse toResponse(CustomerDto dto);
}
