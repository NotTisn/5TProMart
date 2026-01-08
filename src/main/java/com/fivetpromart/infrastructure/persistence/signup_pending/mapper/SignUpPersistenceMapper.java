package com.fivetpromart.infrastructure.persistence.signup_pending.mapper;

import com.fivetpromart.domain.model.PendingRegistration;
import com.fivetpromart.domain.model.PendingRegistration.RegistrationProfileData;
import com.fivetpromart.infrastructure.persistence.signup_pending.SignUpRequestDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SignUpPersistenceMapper {

    // =================================================================
    // 1. DOMAIN -> DBO (Lưu xuống DB)
    // Nhiệm vụ: "Làm phẳng" (Flatten) object lồng nhau ra thành các cột rời rạc
    // =================================================================
    default SignUpRequestDbo toDbo(PendingRegistration domain) {
        if (domain == null) {
            return null;
        }

        // Lấy cục Value Object ra
        RegistrationProfileData profile = domain.getProfileSnapshot();

        return SignUpRequestDbo.builder()
                // Map các trường cơ bản
                .email(domain.getEmail())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .otpHash(domain.getOtpHash())
                .createdAt(domain.getOtpCreatedAt())
                .expiresAt(domain.getOtpExpiresAt())
                .attempts(domain.getOtpAttempts())

                // Map thủ công các trường Profile (Flattening)
                // Cần check null profile để tránh NullPointerException
                .firstName(profile != null ? profile.getFirstName() : null)
                .lastName(profile != null ? profile.getLastName() : null)
                .fullName(profile != null ? profile.getFullName() : null)
                .displayName(profile != null ? profile.getDisplayName() : null)
                .phoneNumber(profile != null ? profile.getPhoneNumber() : null)
                .avatarUrl(profile != null ? profile.getAvatarUrl() : null)
                .bio(profile != null ? profile.getBio() : null)
                .accountType(profile != null ? profile.getAccountType() : null)
                .location(profile != null ? profile.getLocation() : null)
                .dob(profile != null ? profile.getDob() : null)
                .build();
    }

    // =================================================================
    // 2. DBO -> DOMAIN (Đọc từ DB lên)
    // Nhiệm vụ: Gom nhóm (Nested) và gọi hàm "reconstitute"
    // =================================================================
    default PendingRegistration toDomain(SignUpRequestDbo dbo) {
        if (dbo == null) {
            return null;
        }

        // Bước A: Tái tạo Value Object (RegistrationProfileData) trước
        // Lấy từ các cột rời rạc của DBO gom lại thành 1 cục object
        RegistrationProfileData profileSnapshot = RegistrationProfileData.builder()
                .firstName(dbo.getFirstName())
                .lastName(dbo.getLastName())
                .fullName(dbo.getFullName())
                .displayName(dbo.getDisplayName())
                .phoneNumber(dbo.getPhoneNumber())
                .avatarUrl(dbo.getAvatarUrl())
                .bio(dbo.getBio())
                .accountType(dbo.getAccountType())
                .location(dbo.getLocation())
                .dob(dbo.getDob())
                .build();

        // Bước B: Gọi hàm Reconstitution của Domain
        // Đây là cách duy nhất để tạo Domain Entity vì Constructor đang bị khóa (Protected)
        return PendingRegistration.reconstitute(
                dbo.getId(),
                dbo.getEmail(),
                dbo.getUsername(),
                dbo.getPassword(),
                profileSnapshot, // Truyền cục VO vào
                dbo.getOtpHash(),
                dbo.getCreatedAt(),
                dbo.getExpiresAt(),
                dbo.getAttempts()
        );
    }

    // =================================================================
    // 3. UPDATE DBO FROM DOMAIN (Dùng cho Upsert)
    // Thay vì dùng @Mapping, ta viết code Java thuần để kiểm soát logic
    // =================================================================
    default void updateDboFromDomain(SignUpRequestDbo dbo, PendingRegistration domain) {
        if (dbo == null || domain == null) {
            return;
        }

        // A. Cập nhật các trường cơ bản
        // Lưu ý: KHÔNG cập nhật ID và CreatedAt
        dbo.setEmail(domain.getEmail());
        dbo.setUsername(domain.getUsername());
        dbo.setPassword(domain.getPassword());
        dbo.setOtpHash(domain.getOtpHash());
        dbo.setExpiresAt(domain.getOtpExpiresAt());
        dbo.setAttempts(domain.getOtpAttempts());

        // B. Cập nhật (Flatten) lại các trường Profile
        RegistrationProfileData profile = domain.getProfileSnapshot();

        if (profile != null) {
            dbo.setFirstName(profile.getFirstName());
            dbo.setLastName(profile.getLastName());
            dbo.setFullName(profile.getFullName());
            dbo.setDisplayName(profile.getDisplayName());
            dbo.setPhoneNumber(profile.getPhoneNumber());
            dbo.setAvatarUrl(profile.getAvatarUrl());
            dbo.setBio(profile.getBio());
            dbo.setAccountType(profile.getAccountType());
            dbo.setLocation(profile.getLocation());
            dbo.setDob(profile.getDob());
        } else {
            // Nếu Domain không có profile, ta set null các cột tương ứng
            dbo.setFirstName(null);
            dbo.setLastName(null);
            dbo.setFullName(null);
            dbo.setDisplayName(null);
            dbo.setPhoneNumber(null);
            dbo.setAvatarUrl(null);
            dbo.setBio(null);
            dbo.setAccountType(null);
            dbo.setLocation(null);
            dbo.setDob(null);
        }
    }
}