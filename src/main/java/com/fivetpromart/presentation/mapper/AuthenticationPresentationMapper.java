package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.AuthenticationTokensDto;
import com.fivetpromart.application.dto.command.LoginCommand;
import com.fivetpromart.presentation.dto.request.LoginRequest;
import com.fivetpromart.presentation.dto.request.LogoutRequest;
import com.fivetpromart.presentation.dto.response.AuthenticationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationPresentationMapper {
    LoginCommand toLoginDto(LoginRequest domain);
    String toLogoutDto(LogoutRequest domain);
    AuthenticationResponse toResponse(AuthenticationTokensDto dto);
}
