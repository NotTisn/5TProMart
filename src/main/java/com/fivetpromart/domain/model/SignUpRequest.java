package com.fivetpromart.domain.model;

import java.time.Instant;
import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SignUpRequest {

    String id;
    String email;
    String username;
    String password;

    // OTP fields
    String otpHash;
    Instant createdAt;
    Instant expiresAt;
    Integer attempts;

    // Extra profile fields
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

    // ================================
    // STATIC FACTORY (Instead of Builder)
    // ================================
    public static SignUpRequest create(
            String id,
            String email,
            String username,
            String password,
            String firstName,
            String lastName,
            String fullName,
            String displayName,
            String phoneNumber,
            String avatarUrl,
            String bio,
            String accountType,
            String location,
            LocalDate dob
    ) {
        SignUpRequest r = new SignUpRequest();
        r.id = id;
        r.email = email;
        r.username = username;
        r.password = password;
        r.firstName = firstName;
        r.lastName = lastName;
        r.fullName = fullName;
        r.displayName = displayName;
        r.phoneNumber = phoneNumber;
        r.avatarUrl = avatarUrl;
        r.bio = bio;
        r.accountType = accountType;
        r.location = location;
        r.dob = dob;

        // OTP defaults
        r.attempts = 0;
        r.createdAt = Instant.now();
        r.expiresAt = Instant.now().plusSeconds(600);

        return r;
    }

    // ===============================
    // DOMAIN LOGIC (Không đổi tên field)
    // ===============================

    public void assignOtp(String otpHash) {
        this.otpHash = otpHash;
        this.createdAt = Instant.now();
        this.expiresAt = Instant.now().plusSeconds(600);
        this.attempts = 0;
    }

    public void increaseAttempts() {
        this.attempts++;
    }

    public boolean isOtpExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
