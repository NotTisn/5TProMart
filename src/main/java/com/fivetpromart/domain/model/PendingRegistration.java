package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.MaxOtpAttemptsExceededException;
import com.fivetpromart.domain.exception.OtpExpiredException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Constructor bị khóa, chỉ JPA dùng
public class PendingRegistration {

    private String id;
    private String email;
    private String username;
    private String password;

    // Value Object
    private RegistrationProfileData profileSnapshot;

    // OTP State
    private String otpHash;
    private Instant otpCreatedAt;
    private Instant otpExpiresAt;
    private Integer otpAttempts;

    // =================================================================
    // 1. FACTORY: TẠO MỚI (Dùng cho UseCase khi user vừa đăng ký)
    // -> Logic: Tạo ID mới, reset số lần thử về 0
    // =================================================================
    public static PendingRegistration create(String email, String username, String password, RegistrationProfileData profileData) {
        PendingRegistration reg = new PendingRegistration();
        reg.id = UUID.randomUUID().toString(); // <--- Tạo ID MỚI
        reg.email = email;
        reg.username = username;
        reg.password = password;
        reg.profileSnapshot = profileData;

        reg.otpAttempts = 0; // <--- Reset về 0
        return reg;
    }

    // =================================================================
    // 2. RECONSTITUTION: TÁI TẠO (Dùng cho Mapper khi load từ DB lên)
    // -> Logic: Giữ nguyên ID cũ, giữ nguyên số lần thử cũ
    // =================================================================
    public static PendingRegistration reconstitute(
            String id,
            String email,
            String username,
            String password,
            RegistrationProfileData profileSnapshot,
            String otpHash,
            Instant otpCreatedAt,
            Instant otpExpiresAt,
            Integer otpAttempts
    ) {
        PendingRegistration reg = new PendingRegistration();
        reg.id = id; // <--- Giữ nguyên ID CŨ từ DB
        reg.email = email;
        reg.username = username;
        reg.password = password;
        reg.profileSnapshot = profileSnapshot;
        reg.otpHash = otpHash;
        reg.otpCreatedAt = otpCreatedAt;
        reg.otpExpiresAt = otpExpiresAt;
        reg.otpAttempts = otpAttempts; // <--- Giữ nguyên trạng thái cũ
        return reg;
    }

    // =================================================================
    // 3. BUSINESS LOGIC
    // =================================================================
    public void generateOtp(String otpHash, int expirationSeconds) {
        this.otpHash = otpHash;
        this.otpCreatedAt = Instant.now();
        this.otpExpiresAt = Instant.now().plusSeconds(expirationSeconds);
        this.otpAttempts = 0;
    }

    public void renewRegistration(
            String username,
            String password,
            RegistrationProfileData newProfileData,
            String newOtpHash,
            int expirationSeconds
    ) {
        // Cập nhật lại thông tin mới (nếu user đổi ý định nhập tên khác)
        this.username = username;
        this.password = password;
        this.profileSnapshot = newProfileData;

        // Reset lại quy trình OTP như mới
        this.generateOtp(newOtpHash, expirationSeconds);
    }

    public void validateEligibility(int maxAttempts) {
        if (Instant.now().isAfter(this.otpExpiresAt)) {
            // Đảm bảo bạn đã tạo class exception này hoặc dùng RuntimeException
            throw new OtpExpiredException("OTP code has expired.");
        }
        if (this.otpAttempts != null && this.otpAttempts >= maxAttempts) {
            throw new MaxOtpAttemptsExceededException("Too many failed attempts.");
        }
    }

    public void onFailedAttempt() {
        if (this.otpAttempts == null) this.otpAttempts = 0;
        this.otpAttempts++;
    }

    // =================================================================
    // 4. VALUE OBJECT (Inner Class)
    // =================================================================
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistrationProfileData {
        private String firstName;
        private String lastName;
        private String fullName;
        private String displayName;
        private String bio;
        private String phoneNumber;
        private String location;
        private String avatarUrl;
        private String accountType;
        private java.time.LocalDate dob;
    }
}