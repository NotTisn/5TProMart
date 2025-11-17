package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.SignUpRequestDto;
import com.fivetpromart.application.port.in.ISignUpUseCasePort;
import com.fivetpromart.application.port.out.IProfileRepository;
import com.fivetpromart.application.port.out.ISignUpRequestRepository;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // <-- THÊM MỚI
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac; // <-- THÊM MỚI
import javax.crypto.spec.SecretKeySpec; // <-- THÊM MỚI
import java.nio.charset.StandardCharsets; // <-- THÊM MỚI
import java.time.Instant;
import java.util.HexFormat; // <-- THÊM MỚI
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class SignUpUseCase implements ISignUpUseCasePort { // Implement ISignUpUseCase

    private final ISignUpRequestRepository signUpRequestRepository;
    private final IdentityProviderPort identityProviderPort;
    private final IProfileRepository profileRepository;

    @Value("${app.otp.secret}")
    private String otpSecret;

    @Override
    @Transactional
    public void initiateSignUp(SignUpRequestDto request) {
        // 1. Kiểm tra xem email/user đã tồn tại chưa
        if (signUpRequestRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email is already pending verification");
        }

        // 2. Tạo OTP
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));

        // 3. (THAY ĐỔI) Dùng hmacOtp thay vì passwordEncoder
        String otpHash = hmacOtp(otp); // Hash OTP

        // 4. Lưu request tạm
        request.setId(UUID.randomUUID().toString());
        request.setOtpHash(otpHash);
        request.setCreatedAt(Instant.now());
        request.setExpiresAt(Instant.now().plusSeconds(600)); // 10 phút
        request.setAttempts(0);

        signUpRequestRepository.save(request);

        // 5. Gửi email
        //emailServicePort.sendOtpEmail(request.getEmail(), otp);
    }

    @Override
    @Transactional
    public void verifyAndCompleteSignUp(String email, String otp, String password) {
        // 1. Lấy request tạm
        SignUpRequestDto request = signUpRequestRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Invalid email or sign-up request"));

        // 2. Kiểm tra ( hết hạn, số lần thử...)
        if (request.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("OTP expired");
        }

        // 3. (THAY ĐỔI) Dùng hmacOtp và .equals()
        String currentOtpHash = hmacOtp(otp);
        if (!currentOtpHash.equals(request.getOtpHash())) {
            request.setAttempts(request.getAttempts() + 1);
            signUpRequestRepository.save(request);
            throw new IllegalStateException("Invalid OTP");
        }

        // 4. *** XÁC THỰC THÀNH CÔNG ***
        // 4a. Tạo user trên Keycloak
        String keycloakUserId = identityProviderPort.createUser(
                request.getUsername(),
                request.getEmail(),
                password // Dùng password user vừa nhập
        );

        // 4b. Tạo Profile trong DB (dùng logic của Domain Model)
        Profile newProfile = Profile.createDefault(keycloakUserId, request.getEmail());

        // (Cập nhật thêm thông tin từ request nếu cần)
        // newProfile.updateInfo(request.getDisplayName(), ...);

        profileRepository.save(newProfile);

        // 4c. Xóa request tạm
        signUpRequestRepository.deleteByEmail(email);
    }

    private String hmacOtp(String otp) {
        try {
            // SỬA LỖI: "HmacSHA2B5" -> "HmacSHA256"
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key =
                    new SecretKeySpec(otpSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            byte[] raw = mac.doFinal(otp.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(raw);
        } catch (Exception e) {
            throw new RuntimeException("Failed to HMAC OTP", e);
        }
    }
}