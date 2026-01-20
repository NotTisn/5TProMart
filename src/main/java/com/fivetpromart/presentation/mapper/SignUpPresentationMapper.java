package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.RegistrationPendingDto;
import com.fivetpromart.application.dto.command.RegistrationPendingCommand;
import com.fivetpromart.presentation.dto.request.ProfileInitRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignUpPresentationMapper {
    // Dịch DTO của Presentation -> DTO của Application

    @Mapping(target = "fullName", ignore = true)
    RegistrationPendingCommand toApplicationDto(ProfileInitRequest request);
}