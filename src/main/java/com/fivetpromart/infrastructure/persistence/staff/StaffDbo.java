package com.fivetpromart.infrastructure.persistence.staff;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "staff_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffDbo {

    @Id
    @Column(name = "profile_id", length = 50)
    String profileId;

    @Column(name = "user_id", length = 50, nullable = false, unique = true)
    String userId;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    String username;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "account_type", length = 50, nullable = false)
    String accountType;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    String avatarUrl;

    @Column(name = "location", columnDefinition = "TEXT")
    String location;

    @Column(name = "bio", columnDefinition = "TEXT")
    String bio;
}
