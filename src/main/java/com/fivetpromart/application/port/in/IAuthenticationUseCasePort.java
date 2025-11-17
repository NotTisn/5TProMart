package com.fivetpromart.application.port.in;

import com.fivetpromart.domain.model.AuthenticationTokens;

public interface IAuthenticationUseCasePort {
    AuthenticationTokens login(String username, String password);
    AuthenticationTokens refresh(String refreshToken);
    void logout(String refreshToken);
    String createUser(String username, String email, String password);
}