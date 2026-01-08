package com.fivetpromart.infrastructure.identity.keycloak.client;


import com.fivetpromart.infrastructure.config.FeignFormConfig;
import com.fivetpromart.infrastructure.identity.keycloak.dto.ResetPasswordParamDto;
import com.fivetpromart.infrastructure.identity.keycloak.dto.TokenExchangeParamDto;
import com.fivetpromart.infrastructure.identity.keycloak.dto.TokenExchangeResponseDto;
import com.fivetpromart.infrastructure.identity.keycloak.dto.UserCreationParamDto;
import feign.QueryMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.openfeign.SpringQueryMap;


@FeignClient(name = "identity-client", url = "${idp.url}", configuration = FeignFormConfig.class)
@Component
public interface IdentityClient {
    @PostMapping(
            value = "/realms/fivetpro/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    TokenExchangeResponseDto exchangeToken(@RequestBody Map<String, ?> params);

    // Admin token exchange using @RequestParam for proper form encoding
    @PostMapping(
            value = "/realms/fivetpro/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    TokenExchangeResponseDto getAdminToken(@RequestBody Map<String, ?> formParams);

    @PostMapping(value = "/admin/realms/fivetpro/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token, @RequestBody UserCreationParamDto param);

    @PostMapping(
            value = "/admin/realms/{realm}/users/{userId}/execute-actions-email",
            consumes = "application/json")
    ResponseEntity<Void> executeActionsEmail(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("realm") String realm,
            @PathVariable("userId") String userId,
            @RequestBody List<String> actions);

    @PutMapping(
            value = "/admin/realms/fivetpro/users/{userId}/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE // Nên dùng MediaType
    )
    ResponseEntity<?> resetPassword(
            @RequestHeader("Authorization") String bearerToken, // 1. Admin Token
            @PathVariable("userId") String userId, // 3. User ID
            @RequestBody ResetPasswordParamDto param);

    @DeleteMapping(
            value = "/admin/realms/fivetpro/users/{userId}"
    )
    ResponseEntity<?> deleteUser(
            @RequestHeader("authorization") String token,
            @PathVariable String userId);
}
