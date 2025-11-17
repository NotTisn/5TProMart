package com.fivetpromart.presentation.dto.request;

import lombok.Data;

@Data
public class VerifySignUpRequest {
    String email;
    String otp;
    String password; // User phải gửi password ở bước 2 (an toàn hơn)
}