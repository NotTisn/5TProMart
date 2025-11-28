package com.fivetpromart.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationTokens {
    String accessToken;
    String refreshToken;
    String idToken;
    String scope;
}
