package com.fivetpromart.infrastructure.identity.keycloak.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenExchangeParamDto {
    String grant_type;
    String client_id;
    String client_secret;
    String refresh_token;
    String username;
    String password;
    String scope;
}
