package com.fivetpromart.presentation.dto.request;

import lombok.Data;
import java.time.LocalDate;

// Đây là DTO của Presentation, chỉ chứa những gì user gửi ở bước 1
@Data
public class ProfileInitRequest {
    String email;
    String username;
    String firstName;
    String lastName;
    String displayName;
    String phoneNumber;
    String avatarUrl;
    String bio;
    String accountType;
    String location;
    LocalDate dob;
}