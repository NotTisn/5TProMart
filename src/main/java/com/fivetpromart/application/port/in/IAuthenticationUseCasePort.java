package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.AuthenticationTokensDto;
import com.fivetpromart.application.dto.CurrentUserDto;
import com.fivetpromart.application.dto.command.LoginCommand;
import com.fivetpromart.domain.model.AuthenticationTokens;

public interface IAuthenticationUseCasePort {
    AuthenticationTokensDto login(LoginCommand command);

    AuthenticationTokens refresh(String refreshToken);
    void logout(String refreshToken);
    String createUser(String username, String email, String password);
    
    CurrentUserDto getCurrentUser(String userId);
}