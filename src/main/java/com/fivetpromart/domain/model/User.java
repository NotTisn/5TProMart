package com.fivetpromart.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private Boolean enabled;
    private LocalDateTime lastLogin;
    private Integer failedLoginCount;
    private LocalDateTime lockedUntil;
    private String adminNote;
    private Profile userProfile;
    private String googleId;
    private String authProvider;
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
