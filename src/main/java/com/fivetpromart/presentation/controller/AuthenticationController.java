package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.command.LoginCommand;
import com.fivetpromart.application.port.in.IAuthenticationUseCasePort;
import com.fivetpromart.presentation.dto.request.LoginRequest;
import com.fivetpromart.presentation.dto.request.LogoutRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.AuthenticationResponse;
import com.fivetpromart.presentation.mapper.AuthenticationPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationUseCasePort authenticationUseCase;
    private final AuthenticationPresentationMapper mapper;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest request) {
        LoginCommand appDto = mapper.toLoginDto(request);
        AuthenticationResponse appResponse = mapper.toResponse(authenticationUseCase.login(appDto));
        return ApiResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Successfully logged in")
                .data(appResponse)
                .build();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse logout(
            @Valid @RequestBody LogoutRequest request) {
        String appDto = mapper.toLogoutDto(request);
        authenticationUseCase.logout(appDto);
        return ApiResponse.success("Successfully logged out");
    }
}
