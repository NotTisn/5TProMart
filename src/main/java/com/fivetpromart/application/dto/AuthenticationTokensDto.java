package com.fivetpromart.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationTokensDto {
    String accessToken;
    String refreshToken;
    String idToken;
    String scope;
    boolean authenticated;
    LocalDateTime lastLogin;
}