package com.fivetpromart.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
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
}