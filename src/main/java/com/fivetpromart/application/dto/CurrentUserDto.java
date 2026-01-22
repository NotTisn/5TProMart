package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDto {

    private String profileId;
    private String userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;

    // Lưu ý: Trong ảnh format là "DD-MM-YYYY", nên trả về String hoặc dùng @JsonFormat nếu là LocalDate
    private String dateOfBirth;

    private String location;
    private String bio;
    private String accountType;
    private String avatarUrl;
}
