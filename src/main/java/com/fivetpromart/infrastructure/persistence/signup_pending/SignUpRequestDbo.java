package com.fivetpromart.infrastructure.persistence.signup_pending;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "pending_signups")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequestDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    // Password nên được hash trước khi lưu tạm, hoặc không lưu (nếu an toàn hơn)
    // Ở đây ta giả định password được gửi lại ở bước 2

    // OTP fields
    String otpHash;
    Instant createdAt;
    Instant expiresAt;
    Integer attempts;

    // Extra profile fields (chúng ta sẽ dùng để tạo Profile sau)
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