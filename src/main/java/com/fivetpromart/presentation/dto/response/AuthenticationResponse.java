package com.fivetpromart.presentation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AuthenticationResponse {
    String accessToken; // access token trả về từ Keycloak
    String refreshToken; // refresh token trả về từ Keycloak (optional, hoặc lưu httpOnly cookie)
    String idToken; // id token nếu cần
    String scope; // scope từ Keycloak
    boolean authenticated;
    LocalDateTime lastLogin;
}
