package com.fivetpromart.infrastructure.identity.keycloak.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordParamDto {
    String type = "password";
    String value;
    boolean temporary = false;
}
