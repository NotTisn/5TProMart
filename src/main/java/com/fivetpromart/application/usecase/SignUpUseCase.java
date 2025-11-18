package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.ProfileDto;
import com.fivetpromart.application.dto.SignUpRequestDto;
import com.fivetpromart.application.port.in.ISignUpUseCasePort;
import com.fivetpromart.application.port.out.IEmailProviderPort;
import com.fivetpromart.application.port.out.IProfileRepository;
import com.fivetpromart.application.port.out.ISignUpRequestRepository;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.domain.model.Profile;
import com.fivetpromart.domain.model.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value; // <-- THÊM MỚI
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac; // <-- THÊM MỚI
import javax.crypto.spec.SecretKeySpec; // <-- THÊM MỚI
import java.nio.charset.StandardCharsets; // <-- THÊM MỚI
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HexFormat; // <-- THÊM MỚI
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpUseCase implements ISignUpUseCasePort { // Implement ISignUpUseCase

    private final ISignUpRequestRepository signUpRequestRepository;
    private final IdentityProviderPort identityProviderPort;
    private final IProfileRepository profileRepository;
    private final IEmailProviderPort emailProviderPort;

    @Value("${app.otp.secret}")
    private String otpSecret;

    @NonFinal
    @Value("${app.otp.max-attempts:5}")
    int maxOtpAttempts;

    @Override
    public void initiateSignUp(SignUpRequestDto request) {
        // 1. Kiểm tra xem email/user đã tồn tại chưa
        if (signUpRequestRepository.existsByEmail(request.getEmail())) {
            signUpRequestRepository.deleteByEmail(request.getEmail());
        }

        // 2. Tạo OTP
        String otp = generateOtpCode();

        // 3. (THAY ĐỔI) Dùng hmacOtp thay vì passwordEncoder
        String otpHash = hmacOtp(otp); // Hash OTP

        // 4. Lưu request tạm
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .otpHash(otpHash)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(600))
                .attempts(0)
                .username(request.getUsername())
                .password(request.getPassword())
                .dob(request.getDob())
                .bio(request.getBio())
                .accountType(request.getAccountType())
                .email(request.getEmail())
                .avatarUrl(request.getAvatarUrl())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .location(request.getLocation())
                .phoneNumber(request.getPhoneNumber())
                .fullName(request.getFullName())
                .build();

        signUpRequestRepository.save(signUpRequest);

        // 5. Gửi email
        emailProviderPort.sendOtpEmail(request.getEmail(), otp);
    }

    @Override
    public void verifyAndCompleteSignUp(String email, String otp) {
        log.info("Verifying OTP for email: {}", email);

        // 1. Validate Request
        SignUpRequest request = validateSignupRequest(email, otp);

        // 2. Lấy password đã lưu từ DB (Tránh lỗi null khi gửi sang Keycloak)
        String savedPassword = request.getPassword();
        if (savedPassword == null || savedPassword.isEmpty()) {
            log.error("Password missing for pending signup: {}", email);
            throw new IllegalStateException("Password missing in pending request. Please register again.");
        }

        // 3. Tạo user trên Keycloak
        String keycloakUserId = identityProviderPort.createUser(
                request.getUsername(),
                request.getEmail(),
                savedPassword
        );
        log.info("Keycloak User created. ID: {}", keycloakUserId);

        // 4. Tạo Profile trong DB
        createAndSaveProfile(request, keycloakUserId);

        // 5. Dọn dẹp request tạm
        signUpRequestRepository.deleteByEmail(email);
        log.info("Sign-up process completed successfully for email: {}", email);
    }

    // ===================================================================
    // === PRIVATE HELPERS ===
    // ===================================================================

    private SignUpRequest validateSignupRequest(String email, String otp) {
        SignUpRequest request = signUpRequestRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Invalid email or sign-up request"));

        // Check hết hạn
        if (request.getExpiresAt().isBefore(Instant.now())) {
            signUpRequestRepository.deleteByEmail(email);
            throw new IllegalStateException("OTP expired");
        }

        // Check số lần thử
        if (request.getAttempts() >= maxOtpAttempts) {
            signUpRequestRepository.deleteByEmail(email);
            throw new IllegalStateException("Too many failed attempts. Please register again.");
        }

        // Check OTP Hash
        String currentOtpHash = hmacOtp(otp);
        if (!constantTimeEquals(currentOtpHash, request.getOtpHash())) {
            request.increaseAttempts();
            signUpRequestRepository.save(request);
            throw new IllegalStateException("Invalid OTP");
        }

        return request;
    }

    private void createAndSaveProfile(SignUpRequest request, String keycloakUserId) {
        Profile newProfile = Profile.createDefault(keycloakUserId, request.getEmail());

        // Map thêm các trường thông tin cá nhân nếu có
//        if (request.getFirstName() != null) newProfile.setFirstName(request.getFirstName());
//        if (request.getLastName() != null) newProfile.setLastName(request.getLastName());

        profileRepository.save(newProfile);
        log.debug("Profile saved for userId: {}", keycloakUserId);
    }

    public String generateOtpCode() {
        SecureRandom rnd = new SecureRandom();
        int code = rnd.nextInt(900_000) + 100_000; // 6-digit
        return String.valueOf(code);
    }

    private String hmacOtp(String otp) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(otpSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            byte[] raw = mac.doFinal(otp.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(raw);
        } catch (Exception e) {
            log.error("Failed to HMAC OTP", e);
            throw new RuntimeException("Failed to HMAC OTP", e);
        }
    }

    /**
     * So sánh chuỗi an toàn (tránh Timing Attacks)
     */
    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;

        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}