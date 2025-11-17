package com.fivetpromart.infrastructure.identity.keycloak.mapper;

import com.fivetpromart.domain.model.AuthenticationTokens;
import com.fivetpromart.infrastructure.identity.keycloak.dto.TokenExchangeResponseDto;
import org.springframework.stereotype.Component;

@Component
public class KeycloakMapper {
    public AuthenticationTokens toDomain(TokenExchangeResponseDto dto) {
        if (dto == null) return null;
        return AuthenticationTokens.builder()
                .accessToken(dto.getAccessToken())
                .refreshToken(dto.getRefreshToken())
                .idToken(dto.getIdToken())
                .scope(dto.getScope())
                .build();
    }
}
