package com.fivetpromart.infrastructure.identity.keycloak.adapter;

import com.fivetpromart.domain.model.AuthenticationTokens;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.infrastructure.identity.keycloak.client.IdentityClient;
import com.fivetpromart.infrastructure.identity.keycloak.dto.TokenExchangeParamDto;
import com.fivetpromart.infrastructure.identity.keycloak.mapper.KeycloakMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakIdentityAdapter implements IdentityProviderPort {

    private final IdentityClient client;
    private final KeycloakMapper mapper;

    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    @Override
    public AuthenticationTokens login(String email, String password) {
        var req = TokenExchangeParamDto.builder()
                .grant_type("password")
                .client_id(clientId)
                .client_secret(clientSecret)
                .email(email)
                .password(password)
                .scope("openid")
                .build();

        var response = client.exchangeToken(req);
        return mapper.toDomain(response);
    }

    @Override
    public AuthenticationTokens refreshToken(String refreshToken) {
        var req = TokenExchangeParamDto.builder()
                .grant_type("refresh_token")
                .client_id(clientId)
                .client_secret(clientSecret)
                .refresh_token(refreshToken)
                .build();

        return mapper.toDomain(client.exchangeToken(req));
    }

    @Override
    public void logout(String refreshToken) {
        // TODO: call Keycloak logout endpoint
    }

    @Override
    public String createUser(String username, String email, String password) {
        // TODO: implement
        return null;
    }

    @Override
    public void sendActionEmail(String userId, String action) {
        // TODO: implement
    }

    @Override
    public void resetPassword(String userId, String newPassword) {
        // TODO: implement
    }
}
