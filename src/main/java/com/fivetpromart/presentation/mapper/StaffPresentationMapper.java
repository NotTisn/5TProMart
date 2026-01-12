package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.StaffAccountDto;
import com.fivetpromart.application.dto.command.StaffCreationCommand;
import com.fivetpromart.application.dto.command.StaffUpdateCommand;
import com.fivetpromart.presentation.dto.request.StaffRequest;
import com.fivetpromart.presentation.dto.request.StaffUpdateRequest;
import com.fivetpromart.presentation.dto.response.StaffResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffPresentationMapper {
    StaffCreationCommand toCreateCommand(StaffRequest request);
    StaffUpdateCommand toUpdateCommand(StaffUpdateRequest request);
    StaffResponse toResponse(StaffAccountDto dto);
}
