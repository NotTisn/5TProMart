package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.RegistrationPendingDto;
import com.fivetpromart.application.dto.command.RegistrationPendingCommand;

public interface IRegistrationPendingPort {

    // Bước 1: Bắt đầu đăng ký, gửi OTP
    void initiateSignUp(RegistrationPendingCommand request);

    // Bước 2: Xác thực OTP và hoàn tất
    void verifyAndCompleteSignUp(String email, String otp);
}