package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.SignUpRequestDto;
import com.fivetpromart.application.port.in.ISignUpUseCasePort; // <-- Phải tiêm Interface
import com.fivetpromart.presentation.dto.request.ProfileInitRequest;
import com.fivetpromart.presentation.dto.request.VerifySignUpRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.mapper.SignUpPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/signup") // Endpoint riêng cho luồng OTP
@RequiredArgsConstructor
public class SignUpController {

    // 1. Tiêm Input Port (Interface), KHÔNG tiêm implementation
    private final ISignUpUseCasePort signUpUseCase;

    // 2. Cần mapper để dịch DTO của Presentation -> DTO của Application
    private final SignUpPresentationMapper mapper;

    /**
     * Endpoint Bước 1: Bắt đầu đăng ký
     */
    @PostMapping("/initiate")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> initiate(@Valid @RequestBody ProfileInitRequest request) {

        // 3. Dịch Presentation DTO -> Application DTO
        SignUpRequestDto appDto = mapper.toApplicationDto(request);

        // 4. Gọi Use Case với DTO "sạch"
        signUpUseCase.initiateSignUp(appDto);

        return ApiResponse.successNoContent("OTP sent successfully. Please check your email.");
    }

    /**
     * Endpoint Bước 2: Xác thực OTP và hoàn tất
     */
    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public ApiResponse<Void> verify(@Valid @RequestBody VerifySignUpRequest request) {

        // 5. Gọi Use Case
        signUpUseCase.verifyAndCompleteSignUp(
                request.getEmail(),
                request.getOtp()
        );

        return ApiResponse.successNoContent("Account created successfully.");
    }
}