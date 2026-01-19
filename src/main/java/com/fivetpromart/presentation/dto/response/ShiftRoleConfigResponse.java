package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftRoleConfigResponse {
    
    @JsonProperty("id")
    String id;
    
    @JsonProperty("configName")
    String configName;
    
    @JsonProperty("description")
    String description;
    
    @JsonProperty("requirements")
    List<RoleRequirementResponse> requirements;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleRequirementResponse {
        
        @JsonProperty("accountType")
        String accountType;
        
        @JsonProperty("quantity")
        int quantity;
    }
}
