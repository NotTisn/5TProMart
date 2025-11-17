package com.fivetpromart.infrastructure.identity.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenExchangeResponseDto {
    String accessToken;
    String expiresIn;
    String refreshToken;
    String refreshExpiresIn;
    String tokenType;
    String idToken;
    Integer notBeforePolicy;
    String sessionState;
    String scope;
}
