package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.AuthenticationTokensDto;
import com.fivetpromart.application.dto.command.LoginCommand;
import com.fivetpromart.domain.model.AuthenticationTokens;

public interface IdentityProviderPort {

    AuthenticationTokens login(LoginCommand loginCommand);

    AuthenticationTokens refreshToken(String refreshToken);

    void logout(String refreshToken);

    String createUser(String username, String email, String password);

    void deleteUser(String userId);

    void sendActionEmail(String userId, String action);

    void resetPassword(String userId, String newPassword);
}
