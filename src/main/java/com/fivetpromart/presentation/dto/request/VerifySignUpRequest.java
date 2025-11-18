package com.fivetpromart.presentation.dto.request;

import lombok.Data;

@Data
public class VerifySignUpRequest {
    String email;
    String otp;
}