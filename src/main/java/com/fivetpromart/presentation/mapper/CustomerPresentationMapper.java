package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;
import com.fivetpromart.presentation.dto.request.CustomerRequest;
import com.fivetpromart.presentation.dto.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerPresentationMapper {
    CustomerCreationCommand toCommand(CustomerRequest domain);

    @Mapping(target = "customerId", ignore = true)
    CustomerResponse toResponse(CustomerDto dto);

    CustomerUpdateCommand toUpdateDomain(CustomerRequest domain);
}
