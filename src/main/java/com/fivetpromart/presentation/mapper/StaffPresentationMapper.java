package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.StaffAccountDto;
import com.fivetpromart.application.dto.command.StaffCreationCommand;
import com.fivetpromart.application.dto.command.StaffUpdateCommand;
import com.fivetpromart.presentation.dto.request.StaffRequest;
import com.fivetpromart.presentation.dto.request.StaffUpdateRequest;
import com.fivetpromart.presentation.dto.response.StaffResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffPresentationMapper {
    StaffCreationCommand toCreateCommand(StaffRequest request);
    StaffUpdateCommand toUpdateCommand(StaffUpdateRequest request);

    @Mapping(target = "userId", ignore = true)
    StaffResponse toResponse(StaffAccountDto dto);
}
