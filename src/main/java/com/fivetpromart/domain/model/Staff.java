package com.fivetpromart.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Staff {
    private String profileId;
    private String userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String accountType;  // "SalesStaff", "WarehouseStaff"
    private String avatarUrl;
    private String location;
    private String bio;
    private Boolean isActive = true;

    // =================================================================
    // 1. FACTORY: CREATE NEW STAFF
    // =================================================================
    public static Staff create(
            String userId,
            String username,
            String fullName,
            String email,
            String phoneNumber,
            String accountType,
            LocalDate dateOfBirth,
            String location,
            String bio
    ) {
        // TODO: Add validation
        // TODO: - username, email, fullName, phoneNumber, accountType are required
        // TODO: - accountType must be "SalesStaff" or "WarehouseStaff"
        // TODO: - Throw EmptyFieldException if required fields are null/blank
        
        Staff staff = new Staff();
        staff.profileId = UUID.randomUUID().toString();
        staff.userId = userId;
        staff.username = username;
        staff.fullName = fullName;
        staff.email = email;
        staff.phoneNumber = phoneNumber;
        staff.accountType = accountType;
        staff.dateOfBirth = dateOfBirth;
        staff.location = location;
        staff.bio = bio;
        staff.avatarUrl = null;  // Default avatar
        
        return staff;
    }

    // =================================================================
    // 2. FACTORY: RECONSTITUTE (Load from DB)
    // =================================================================
    public static Staff reconstitute(
            String profileId,
            String userId,
            String username,
            String fullName,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth,
            String accountType,
            String avatarUrl,
            String location,
            String bio
    ) {
        Staff staff = new Staff();
        staff.profileId = profileId;
        staff.userId = userId;
        staff.username = username;
        staff.fullName = fullName;
        staff.email = email;
        staff.phoneNumber = phoneNumber;
        staff.dateOfBirth = dateOfBirth;
        staff.accountType = accountType;
        staff.avatarUrl = avatarUrl;
        staff.location = location;
        staff.bio = bio;
        return staff;
    }

    // =================================================================
    // 3. BUSINESS: UPDATE INFO
    // =================================================================
    public void updateInfo(
            String fullName,
            String email,
            String phoneNumber,
            String accountType,
            LocalDate dateOfBirth,
            String location,
            String bio
    ) {
        // TODO: Add validation
        // TODO: - Validate required fields
        // TODO: - Only update if new value is not null/blank
        
        if (fullName != null && !fullName.isBlank()) this.fullName = fullName;
        if (email != null && !email.isBlank()) this.email = email;
        if (phoneNumber != null && !phoneNumber.isBlank()) this.phoneNumber = phoneNumber;
        if (accountType != null && !accountType.isBlank()) this.accountType = accountType;
        if (dateOfBirth != null) this.dateOfBirth = dateOfBirth;
        if (location != null) this.location = location;
        if (bio != null) this.bio = bio;
    }

    // =================================================================
    // 4. BUSINESS: UPDATE AVATAR
    // =================================================================
    public void updateAvatar(String avatarUrl) {
        // TODO: Validate avatar URL format if needed
        this.avatarUrl = avatarUrl;
    }

    // Soft delete methods
    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public boolean isActive() {
        return this.isActive != null && this.isActive;
    }
}
