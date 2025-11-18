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
    private String email;
    private String avatarUrl;
    private String userId;
    private String displayName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private String accountType;
    private String location;

    /**
     * Factory method - Business logic nằm ở Domain Model
     */
    public static Profile createDefault(String userId, String email) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        return Profile.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)              // ← Quan trọng
                .email(email)
                .displayName(email)
                .fullName("")
                .accountType("USER")
                .build();
    }

    /**
     * Business method - Cập nhật thông tin profile
     */
    public Profile updateInfo(String username, String firstName, String lastName,
                              String displayName, String phoneNumber) {
        return Profile.builder()
                .id(this.id)
                .userId(this.userId)
                .email(this.email)
                .firstName(firstName)
                .lastName(lastName)
                .displayName(displayName != null ? displayName : this.displayName)
                .fullName(buildFullName(firstName, lastName))
                .phoneNumber(phoneNumber)
                .accountType(this.accountType)
                .bio(this.bio)
                .avatarUrl(this.avatarUrl)
                .location(this.location)
                .build();
    }

    private String buildFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) {
            return "";
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }
}
