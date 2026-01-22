package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.AuthenticationTokensDto;
import com.fivetpromart.application.dto.CurrentUserDto;
import com.fivetpromart.application.dto.command.LoginCommand;
import com.fivetpromart.application.mapper.AuthenticationDataMapper;
import com.fivetpromart.application.port.in.IAuthenticationUseCasePort;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.application.port.out.IStaffRepository;
import com.fivetpromart.domain.model.AuthenticationTokens;
import com.fivetpromart.domain.model.Staff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationUseCase implements IAuthenticationUseCasePort {

    private final IdentityProviderPort identityProviderPort;
    private final IStaffRepository staffRepository;
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

    @Override
    public CurrentUserDto getCurrentUser(String userId) {
        log.info("Getting current user information for userId: {}", userId);
        
        // Find staff by Keycloak userId
        Staff staff = staffRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Staff not found for userId: {}", userId);
                    return new RuntimeException("Staff account not found");
                });
        
        log.info("Found staff: {} ({})", staff.getFullName(), staff.getAccountType());
        
        // Build response with staff information
        return CurrentUserDto.builder()
                .userId(userId)
                .staffId(staff.getUserId())
                .username(staff.getUsername())
                .email(staff.getEmail())
                .birthDate(staff.getDateOfBirth())
                .location(staff.getLocation())
                .fullName(staff.getFullName())
                .phoneNumber(staff.getPhoneNumber())
                .roles(List.of(staff.getAccountType())) // Single role for now
                .authenticated(true)
                .build();
    }
}