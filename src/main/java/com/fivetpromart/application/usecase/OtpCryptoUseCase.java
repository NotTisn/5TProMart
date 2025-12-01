package com.fivetpromart.application.usecase;

import com.fivetpromart.application.port.in.IOtpCryptoUseCasePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HexFormat;

@Component
@Slf4j
public class OtpCryptoUseCase implements IOtpCryptoUseCasePort {

    @Value("${app.otp.secret}")
    private String otpSecret;

    private static final String HMAC_ALGO = "HmacSHA256";

    // Tạo OTP ngẫu nhiên 6 số
    public String generateOtpCode() {
        SecureRandom rnd = new SecureRandom();
        int code = rnd.nextInt(900_000) + 100_000;
        return String.valueOf(code);
    }

    // Hash OTP
    public String hmacOtp(String otp) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec key = new SecretKeySpec(otpSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
            mac.init(key);
            byte[] raw = mac.doFinal(otp.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(raw);
        } catch (Exception e) {
            log.error("Failed to HMAC OTP", e);
            throw new RuntimeException("Crypto error", e);
        }
    }

    // So sánh an toàn (chống Timing Attack)
    public boolean constantTimeEquals(String inputHash, String storedHash) {
        if (inputHash == null || storedHash == null) return false;
        if (inputHash.length() != storedHash.length()) return false;

        int result = 0;
        for (int i = 0; i < inputHash.length(); i++) {
            result |= inputHash.charAt(i) ^ storedHash.charAt(i);
        }
        return result == 0;
    }
}