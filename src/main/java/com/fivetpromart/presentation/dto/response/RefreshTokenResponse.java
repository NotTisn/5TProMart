package com.fivetpromart.presentation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenResponse {
    private String token;

    public RefreshTokenResponse(String accessToken) {
    }
}
