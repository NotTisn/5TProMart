package com.fivetpromart.application.port.in;

public interface IEmailUseCasePort {
    void sendOtpEmail(String toEmail, String otp);
}