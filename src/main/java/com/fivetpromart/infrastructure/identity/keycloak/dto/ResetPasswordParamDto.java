package com.fivetpromart.infrastructure.identity.keycloak.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordParamDto {
    @Builder.Default
    String type = "password";
    String value;
    @Builder.Default
    boolean temporary = false;
}
