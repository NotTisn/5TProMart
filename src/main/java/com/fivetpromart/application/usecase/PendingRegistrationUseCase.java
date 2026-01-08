package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.command.RegistrationPendingCommand;
import com.fivetpromart.application.port.in.IRegistrationPendingPort;
import com.fivetpromart.application.port.out.IEmailProviderPort;
import com.fivetpromart.application.port.out.IProfileRepository;
import com.fivetpromart.application.port.out.ISignUpRequestRepository;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.domain.exception.DomainException;
import com.fivetpromart.domain.exception.InvalidOperationException;
import com.fivetpromart.domain.model.PendingRegistration;
import com.fivetpromart.domain.model.PendingRegistration.RegistrationProfileData; // Import Value Object
import com.fivetpromart.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PendingRegistrationUseCase implements IRegistrationPendingPort {

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
    public void initiateSignUp(RegistrationPendingCommand request) {
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

        // 2. CHIẾN THUẬT UPSERT
        Optional<PendingRegistration> existingOpt = signUpRequestRepository.findByEmail(request.getEmail());

        PendingRegistration pendingRegistration;

        if (existingOpt.isPresent()) {
            // CASE A: Đã có -> Cập nhật (renew)
            pendingRegistration = existingOpt.get();
            log.info("Renewing existing registration for email: {}", request.getEmail());

            pendingRegistration.renewRegistration(
                    request.getUsername(),
                    request.getPassword(),
                    profileSnapshot,
                    hashedOtp,
                    otpExpirationSeconds
            );

        } else {
            // CASE B: Chưa có -> Tạo mới
            log.info("Creating new registration for email: {}", request.getEmail());

            pendingRegistration = PendingRegistration.create(
                    request.getEmail(),
                    request.getUsername(),
                    request.getPassword(),
                    profileSnapshot
            );

            // QUAN TRỌNG: Phải generate OTP cho entity mới
            pendingRegistration.generateOtp(hashedOtp, otpExpirationSeconds);
        }

        // 3. Lưu (JPA tự biết INSERT hay UPDATE)
        signUpRequestRepository.save(pendingRegistration);

        // 4. Gửi Email
        emailProviderPort.sendOtpEmail(request.getEmail(), rawOtp);
    }

    @Override
    @Transactional
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
            // Re-throw the original exception if it's already a domain exception
            if (e instanceof DomainException) {
                throw (DomainException) e;
            }
            throw new InvalidOperationException("Failed to complete sign up: " + e.getMessage());
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