package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.AuthenticationTokensDto;
import com.fivetpromart.application.dto.command.LoginCommand;
import com.fivetpromart.application.mapper.AuthenticationDataMapper;
import com.fivetpromart.application.port.in.IAuthenticationUseCasePort;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.domain.model.AuthenticationTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCase implements IAuthenticationUseCasePort {

    private final IdentityProviderPort identityProviderPort;
    private final AuthenticationDataMapper mapper;

    @Override
    public AuthenticationTokensDto login(LoginCommand command) {
        AuthenticationTokens tokens = identityProviderPort.login(command);

        return AuthenticationTokensDto.builder()
                // Copy từ Domain
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .idToken(tokens.getIdToken())
                .scope(tokens.getScope())

                // Logic bổ sung của Application
                .authenticated(true)
                .lastLogin(LocalDateTime.now())
                .build();
    }

    @Override
    public AuthenticationTokens refresh(String refreshToken) {
        return identityProviderPort.refreshToken(refreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        identityProviderPort.logout(refreshToken);
    }

    @Override
    public String createUser(String username, String email, String password) {
        return identityProviderPort.createUser(username, email, password);
    }
}