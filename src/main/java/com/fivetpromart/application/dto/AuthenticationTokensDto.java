package com.fivetpromart.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationTokensDto {
    String accessToken;
    String refreshToken;
    String idToken;
    String scope;
}