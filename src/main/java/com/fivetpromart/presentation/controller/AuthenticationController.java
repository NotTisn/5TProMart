package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.command.LoginCommand;
import com.fivetpromart.application.port.in.IAuthenticationUseCasePort;
import com.fivetpromart.domain.model.AuthenticationTokens;
import com.fivetpromart.infrastructure.helper.HttpOnlyCookieHelper;
import com.fivetpromart.presentation.dto.request.LoginRequest;
import com.fivetpromart.presentation.dto.request.LogoutRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.AuthenticationResponse;
import com.fivetpromart.presentation.dto.response.RefreshTokenResponse;
import com.fivetpromart.presentation.mapper.AuthenticationPresentationMapper;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationUseCasePort authenticationUseCase;
    private final AuthenticationPresentationMapper mapper;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        LoginCommand appDto = mapper.toLoginDto(request);
        AuthenticationResponse appResponse = mapper.toResponse(authenticationUseCase.login(appDto));
        // 2. Lưu refresh token vào HttpOnly cookie
        HttpOnlyCookieHelper.addHttpOnlyCookie(
                response, "refresh_token", appResponse.getRefreshToken(), 7 * 24 * 60 * 60 // 7 ngày
        );

        // 3. Trả body JSON (không cần refreshToken nữa nếu muốn)
        appResponse.setRefreshToken(null);
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

    @PostMapping("/refresh-token")
    public ApiResponse<Map<String, String>> refreshToken(
            HttpServletResponse response,
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            HttpOnlyCookieHelper.deleteHttpOnlyCookie(response, "refresh_token");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token missing.");
        }

        try {
            AuthenticationTokens authResponse = authenticationUseCase.refresh(refreshToken);

            HttpOnlyCookieHelper.addHttpOnlyCookie(
                    response,
                    "refresh_token",
                    authResponse.getRefreshToken(), // Lấy RT mới từ response
                    30 * 24 * 60 * 60 // 30 ngày
            );

            return ApiResponse.<Map<String, String>>builder()
                    .data(Map.of("accessToken", authResponse.getAccessToken()))
                    .build();

        } catch (FeignException.BadRequest | FeignException.Unauthorized e) {
            HttpOnlyCookieHelper.deleteHttpOnlyCookie(response, "refresh_token");

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Session expired. Please log in again.");
        }
    }
}
