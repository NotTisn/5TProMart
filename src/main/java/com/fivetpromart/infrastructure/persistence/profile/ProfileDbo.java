package com.fivetpromart.infrastructure.persistence.profile; // Đổi tên package nếu cần, ví dụ: .persistence.entity

import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "profiles")
public class ProfileDbo {

    @Id
    String id;

    @Column(name = "user_id", unique = true, nullable = false)
    String userId;

    @Column(unique = true)
    String username;

    @Column(unique = true)
    String email;

    String displayName;
    String firstName;
    String lastName;
    String fullName;
    String phoneNumber;
    String avatarUrl;
    String accountType;
    String location;

    @Column(columnDefinition = "TEXT")
    String bio;

    @Column(name = "date_of_birth")
    LocalDate dob;

}