package com.fivetpromart.infrastructure.identity.keycloak.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fivetpromart.domain.model.AuthenticationTokens;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.infrastructure.error.AppException;
import com.fivetpromart.infrastructure.error.ErrorCode;
import com.fivetpromart.infrastructure.identity.keycloak.client.IdentityClient;
import com.fivetpromart.infrastructure.identity.keycloak.dto.Credential;
import com.fivetpromart.infrastructure.identity.keycloak.dto.TokenExchangeParamDto;
import com.fivetpromart.infrastructure.identity.keycloak.dto.UserCreationParamDto;
import com.fivetpromart.infrastructure.identity.keycloak.mapper.KeycloakMapper;
import feign.FeignException;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakIdentityAdapter implements IdentityProviderPort {

    private final IdentityClient client;
    private final KeycloakMapper mapper;
    private final ObjectMapper objectMapper; // <--- THÊM: Để log JSON body

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
//                .refresh_token(refreshToken)
                .build();

        return mapper.toDomain(client.exchangeToken(req));
    }

    @Override
    public void logout(String refreshToken) {
        // TODO: call Keycloak logout endpoint
    }

    @Override
    public String createUser(String username, String email, String password) {
        log.info("START createUser: username={}, email={}", username, email);

        try {
            // 1. Get admin token
            log.debug("Step 1: Requesting Admin Client Credentials Token...");
            var token = client.exchangeToken(
                    TokenExchangeParamDto.builder()
                            .grant_type("client_credentials")
                            .client_id(clientId)
                            .client_secret(clientSecret)
                            .scope("openid")
                            .build()
            );
            log.debug("Step 1 Success: Admin token obtained.");

            // 2. Build Request Object
            var userCreationRequest = UserCreationParamDto.builder()
                    .username(username)
                    .email(email)
                    .enabled(true)
                    .emailVerified(true)
                    .firstName(username) // Thêm cái này để tránh null
                    .lastName(username)  // Thêm cái này để tránh null
                    .credentials(
                            List.of(
                                    Credential.builder()
                                            .type("password")
                                            .temporary(false)
                                            .value(password)
                                            .build()
                            )
                    )
                    .build();

            // --- LOG JSON BODY SẼ GỬI ĐI (QUAN TRỌNG) ---
            try {
                String jsonBody = objectMapper.writeValueAsString(userCreationRequest);
                log.info("Step 2: Sending Payload to Keycloak: {}", jsonBody);
            } catch (Exception ex) {
                log.warn("Could not serialize creation request for logging", ex);
            }
            // --------------------------------------------

            // 3. Call Keycloak API
            var creationResponse = client.createUser(
                    "Bearer " + token.getAccessToken(),
                    userCreationRequest
            );

            log.info("Step 3 Success: Keycloak responded with Status Code: {}", creationResponse.getStatusCode());

            // 4. Extract userId
            String userId = extractUserId(creationResponse);
            log.info("FINISHED createUser: User created successfully with userId={}", userId);

            return userId;

        } catch (FeignException e) {
            // Log chi tiết lỗi từ Keycloak trả về
            log.error("KEYCLOAK ERROR during createUser. Status: {}", e.status());
            log.error("Response Body: {}", e.contentUTF8());

            String errorMessage = parseKeycloakError(e);
            throw new RuntimeException("Failed to create user in Keycloak: " + errorMessage, e);
        } catch (Exception e) {
            log.error("Unexpected error in createUser: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error creating user", e);
        }
    }

    @Override
    public void sendActionEmail(String userId, String action) {
        // TODO: implement
    }

    @Override
    public void resetPassword(String userId, String newPassword) {
        // TODO: implement
    }

    /**
     * Extract userId from Keycloak creation response
     */
    private String extractUserId(ResponseEntity<?> response) {
        // Xử lý an toàn hơn
        try {
            String location = response.getHeaders().get("Location").getFirst();
            String[] splitedStr = location.split("/");
            return splitedStr[splitedStr.length - 1];
        } catch (Exception e) {
            log.error("Không thể trích xuất UserId từ header 'Location': {}", response.getHeaders().get("Location"), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Không thể lấy UserId sau khi tạo user.");
        }
    }

    /**
     * Parse Keycloak error from FeignException
     */
    private String parseKeycloakError(FeignException e) {
        if (e.status() == 409) {
            return "User already exists";
        } else if (e.status() == 400) {
            return "Invalid user data: " + e.contentUTF8();
        } else if (e.status() == 401 || e.status() == 403) {
            return "Unauthorized to create user - check client credentials";
        } else {
            return e.getMessage();
        }
    }

    /**
     * Read response body safely
     */
    private String readResponseBody(Response response) {
        try {
            if (response.body() != null) {
                return new String(response.body().asInputStream().readAllBytes());
            }
            return "";
        } catch (IOException e) {
            log.warn("Failed to read response body", e);
            return "";
        }
    }
}
