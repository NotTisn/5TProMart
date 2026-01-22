package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoleConfigResponse {
    
    @JsonProperty("id")
    String id;
    
    @JsonProperty("configName")
    String configName;
    
    @JsonProperty("isActive")
    boolean isActive;
}
