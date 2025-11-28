package com.fivetpromart.application.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.time.LocalDate;

// Đây là DTO "sạch" mà Lớp Application sử dụng
// Nó giống hệt DBO, nhưng sự tách biệt này là quan trọng
@Data
@Builder
public class RegistrationPendingDto {
    String id;
    String email;
    String username;
    String password;
    String otpHash;
    Instant createdAt;
    Instant expiresAt;
    Integer attempts;
    String firstName;
    String lastName;
    String fullName;
    String displayName;
    String phoneNumber;
    String avatarUrl;
    String bio;
    String accountType;
    String location;
    LocalDate dob;
}