package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.SignUpRequestDto;

public interface ISignUpUseCasePort {

    // Bước 1: Bắt đầu đăng ký, gửi OTP
    void initiateSignUp(SignUpRequestDto request);

    // Bước 2: Xác thực OTP và hoàn tất
    void verifyAndCompleteSignUp(String email, String otp);
}