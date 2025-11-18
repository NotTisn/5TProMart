package com.fivetpromart.application.port.out;

public interface IEmailProviderPort {
    void sendOtpEmail(String toEmail, String otp);

}