package com.fivetpromart.application.port.in;

public interface IOtpCryptoUseCasePort {
    // Tạo OTP ngẫu nhiên 6 số
    String generateOtpCode();

    // Hash OTP
    String hmacOtp(String otp);

    // So sánh an toàn (chống Timing Attack)
    boolean constantTimeEquals(String inputHash, String storedHash);
}
