package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.SignUpRequestDto;
import com.fivetpromart.application.port.in.ISignUpUseCasePort;
import com.fivetpromart.application.port.out.IEmailProviderPort;
import com.fivetpromart.application.port.out.IProfileRepository;
import com.fivetpromart.application.port.out.ISignUpRequestRepository;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.domain.model.PendingRegistration;
import com.fivetpromart.domain.model.PendingRegistration.RegistrationProfileData; // Import Value Object
import com.fivetpromart.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PendingRegistrationUseCase implements ISignUpUseCasePort {

    private final ISignUpRequestRepository signUpRequestRepository;
    private final IdentityProviderPort identityProviderPort;
    private final IProfileRepository profileRepository;
    private final IEmailProviderPort emailProviderPort;
    private final OtpCryptoUseCase otpCryptoService;

    @Value("${app.otp.max-attempts:5}")
    private int maxOtpAttempts;

    @Value("${app.otp.expiration-seconds:600}")
    private int otpExpirationSeconds;

    @Override
    @Transactional
    public void initiateSignUp(SignUpRequestDto request) {
        log.info("Initiating sign up for email: {}", request.getEmail());

        // 1. Chuẩn bị dữ liệu Profile & OTP
        RegistrationProfileData profileSnapshot = RegistrationProfileData.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullName(request.getFullName())
                .bio(request.getBio())
                .phoneNumber(request.getPhoneNumber())
                .location(request.getLocation())
                .avatarUrl(request.getAvatarUrl())
                .build();

        String rawOtp = otpCryptoService.generateOtpCode();
        String hashedOtp = otpCryptoService.hmacOtp(rawOtp);

        // 2. CHIẾN THUẬT UPSERT (Thay thế đoạn Delete -> Save cũ)
        // Tìm xem có request nào đang chờ không?
        PendingRegistration pendingRegistration = signUpRequestRepository.findByEmail(request.getEmail())
                .map(existingReg -> {
                    // CASE A: Đã có -> Cập nhật lại (Renew)
                    log.info("Renewing existing registration for email: {}", request.getEmail());
                    existingReg.renewRegistration(
                            request.getUsername(),
                            request.getPassword(),
                            profileSnapshot,
                            hashedOtp,
                            otpExpirationSeconds
                    );
                    return existingReg;
                })
                .orElseGet(() -> {
                    // CASE B: Chưa có -> Tạo mới (Create)
                    log.info("Creating new registration for email: {}", request.getEmail());
                    PendingRegistration newReg = PendingRegistration.create(
                            request.getEmail(),
                            request.getUsername(),
                            request.getPassword(),
                            profileSnapshot
                    );
                    newReg.generateOtp(hashedOtp, otpExpirationSeconds);
                    return newReg;
                });

        // 3. Chỉ cần gọi Save 1 lần duy nhất
        // JPA tự biết: Nếu có ID cũ -> Update. Nếu ID mới -> Insert.
        signUpRequestRepository.save(pendingRegistration);

        // 4. Gửi Email
        emailProviderPort.sendOtpEmail(request.getEmail(), rawOtp);
    }

    @Override
    public void verifyAndCompleteSignUp(String email, String otp) {
        log.info("Verifying OTP for email: {}", email);

        // 1. Lấy dữ liệu
        PendingRegistration pendingReg = signUpRequestRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Registration request not found or expired"));

        // 2. Domain Validation (Entity tự kiểm tra xem còn hạn hay quá số lần thử không)
        try {
            pendingReg.validateEligibility(maxOtpAttempts);
        } catch (IllegalStateException e) {
            // Vi phạm rule (Hết hạn/Quá lần thử) -> Xóa luôn bắt làm lại
            signUpRequestRepository.deleteByEmail(email);
            throw e;
        }

        // 3. Kiểm tra OTP Hash (Infrastructure Logic)
        String inputHash = otpCryptoService.hmacOtp(otp);
        if (!otpCryptoService.constantTimeEquals(inputHash, pendingReg.getOtpHash())) {
            // Logic sai OTP: Entity tự tăng count
            pendingReg.onFailedAttempt();
            signUpRequestRepository.save(pendingReg);
            throw new IllegalStateException("Invalid OTP code");
        }

        // 4. BẮT ĐẦU QUY TRÌNH TẠO USER (Distributed Transaction Pattern)
        String keycloakUserId = null;

        try {
            // A. Tạo User trên Keycloak
            // Dùng getPassword() (raw) chứ không phải Hash, vì Keycloak cần pass gốc để tạo user
            keycloakUserId = identityProviderPort.createUser(
                    pendingReg.getUsername(),
                    pendingReg.getEmail(),
                    pendingReg.getPassword()
            );
            log.info("Keycloak User created: {}", keycloakUserId);

            // B. Tạo Profile trong DB Local (Mapping chi tiết)
            createAndSaveLocalProfile(pendingReg, keycloakUserId);

            // C. Thành công -> Xóa Request tạm
            signUpRequestRepository.deleteByEmail(email);

            log.info("Sign up completed successfully for user: {}", keycloakUserId);

        } catch (Exception e) {
            log.error("Sign up failed. Rolling back...", e);
            // --- COMPENSATING TRANSACTION (ROLLBACK THỦ CÔNG) ---
            if (keycloakUserId != null) {
                try {
                    identityProviderPort.deleteUser(keycloakUserId);
                    log.warn("Rolled back user in Keycloak: {}", keycloakUserId);
                } catch (Exception rollbackEx) {
                    log.error("CRITICAL: Failed to rollback Keycloak user: {}", keycloakUserId, rollbackEx);
                    // TODO: Đẩy vào Dead Letter Queue để xử lý sau
                }
            }
            throw new RuntimeException("Failed to complete sign up. Please try again.", e);
        }
    }

    // --- Private Helpers ---

    private void createAndSaveLocalProfile(PendingRegistration reg, String userId) {
        // 1. Tạo Profile mặc định (Id, Email)
        Profile profile = Profile.createDefault(userId, reg.getEmail());

        // Lấy snapshot dữ liệu ra
        RegistrationProfileData data = reg.getProfileSnapshot();
        if (data != null) {
            // 2. Mapping vào từng nhóm method cụ thể (Granular Methods)

            // Nhóm định danh
            profile.changeIdentityInfo(
                    data.getFirstName(),
                    data.getLastName(),
                    data.getFullName() // Hoặc displayName
            );

            // Nhóm liên lạc
            profile.updateContactInfo(
                    data.getPhoneNumber(),
                    data.getLocation()
            );

            // Nhóm hiển thị công khai
            profile.updatePublicProfile(
                    data.getBio(),
                    data.getAvatarUrl()
            );
        }

        profileRepository.save(profile);
    }
}