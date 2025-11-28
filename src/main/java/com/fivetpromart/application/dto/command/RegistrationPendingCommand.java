package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class RegistrationPendingCommand {
    // --- Nhóm bắt buộc ---
    String email;
    String username;
    String password;

    // --- Nhóm Profile (Optional) ---
    String firstName;
    String lastName;
    String fullName;
    String displayName;
    String phoneNumber;
    String avatarUrl;
    String bio;
    String location;
    LocalDate dob;

}