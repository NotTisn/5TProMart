package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;
import com.fivetpromart.presentation.dto.request.CustomerRequest;
import com.fivetpromart.presentation.dto.response.CustomerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerPresentationMapper {
    CustomerCreationCommand toDto(CustomerRequest domain);
    CustomerResponse toResponse(CustomerDto dto);

    CustomerUpdateCommand toUpdateDto(CustomerRequest domain);
}
