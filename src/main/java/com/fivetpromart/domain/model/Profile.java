package com.fivetpromart.domain.model;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Profile {

    private String id;
    private String bio;
    private String avatarUrl;
    private String userId;
    private String displayName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private String accountType;
    private String location;

    public static Profile createDefault(String userId, String email) {
        return Profile.builder()
                .id(UUID.randomUUID().toString()) // Tự tạo ID cho DB nội bộ
                .userId(userId) // Link tới ID của Keycloak
                .displayName(email) // Mặc định dùng email làm tên hiển thị
                .fullName("") // Khởi tạo là rỗng
                .accountType("USER") // Đặt loại tài khoản mặc định
                // (Các trường khác sẽ là null theo mặc định của Builder)
                .build();
    }
}
