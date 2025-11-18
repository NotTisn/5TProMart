package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO dùng trong Application Layer
 * Dùng để transfer data giữa Use Cases và Ports
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProfileDto {
    private String id;
    private String userId;
    private String email;
    private String username;
    private String displayName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String phoneNumber;
    private String accountType;
    private String location;
}