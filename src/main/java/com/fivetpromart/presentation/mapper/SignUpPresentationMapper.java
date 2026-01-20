package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.RegistrationPendingDto;
import com.fivetpromart.application.dto.command.RegistrationPendingCommand;
import com.fivetpromart.presentation.dto.request.ProfileInitRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignUpPresentationMapper {
    // Dịch DTO của Presentation -> DTO của Application
    RegistrationPendingCommand toApplicationDto(ProfileInitRequest request);
}