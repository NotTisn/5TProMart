package com.fivetpromart.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

// Bỏ @Builder ở class level để kiểm soát chặt chẽ việc khởi tạo
// Bỏ @AllArgsConstructor để ép dùng Factory Method
@Getter
@NoArgsConstructor // Cần thiết cho JPA/Jackson, nhưng nên để access level thấp nếu có thể
public class Profile {

    private String id;
    private String userId; // Foreign Key reference
    private String email;  // Tốt nhất nên là Value Object: private Email email;

    // Thông tin cá nhân
    private String firstName;
    private String lastName;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private String phoneNumber;
    private String accountType;
    private String location;

    // Computed field (không cần lưu trong DB nếu không cần thiết, hoặc map bằng setter)
    public String getFullName() {
        return buildFullName(this.firstName, this.lastName);
    }

    // --- CONSTRUCTOR: Private để ép dùng Factory ---
    private Profile(String userId, String email) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.email = email;
        this.displayName = email; // Default display name
        this.accountType = "USER";
        // Các trường khác null hoặc default
    }

    // --- FACTORY METHOD ---
    public static Profile createDefault(String userId, String email) {
        // Validation (Invariant Guard)
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("userId is required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email is required");

        return new Profile(userId, email);
    }

    // --- BUSINESS METHODS (Hành vi cụ thể) ---

    // 1. Cập nhật thông tin định danh
    public void changeIdentityInfo(String firstName, String lastName, String displayName) {
        this.firstName = firstName;
        this.lastName = lastName;
        // Logic nghiệp vụ: Nếu không truyền displayName thì giữ nguyên hoặc lấy theo tên
        if (displayName != null && !displayName.isBlank()) {
            this.displayName = displayName;
        }
    }

    // 2. Cập nhật thông tin liên lạc
    public void updateContactInfo(String phoneNumber, String location) {
        // Có thể validate số điện thoại ở đây
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    // 3. Cập nhật hồ sơ công khai
    public void updatePublicProfile(String bio, String avatarUrl) {
        this.bio = bio;
        this.avatarUrl = avatarUrl;
    }

    // Helper logic
    private String buildFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) return "";
        return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
    }
}