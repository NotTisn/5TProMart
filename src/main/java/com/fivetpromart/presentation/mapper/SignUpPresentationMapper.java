package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.RegistrationPendingDto;
import com.fivetpromart.application.dto.command.RegistrationPendingCommand;
import com.fivetpromart.presentation.dto.request.ProfileInitRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpPresentationMapper {
    // Dịch DTO của Presentation -> DTO của Application
    RegistrationPendingCommand toApplicationDto(ProfileInitRequest request);
}