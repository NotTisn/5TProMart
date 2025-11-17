package com.fivetpromart.application.usecase;

import com.fivetpromart.application.port.in.IAuthenticationUseCasePort;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.domain.model.AuthenticationTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCase implements IAuthenticationUseCasePort {

    private final IdentityProviderPort identityProviderPort;

    @Override
    public AuthenticationTokens login(String username, String password) {
        return identityProviderPort.login(username, password);
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