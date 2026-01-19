package com.fivetpromart.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoleConfigRequest {
    
    @NotBlank(message = "Config name is required")
    @JsonProperty("configName")
    String configName;
    
    @JsonProperty("description")
    String description;
    
    @NotEmpty(message = "Requirements cannot be empty")
    @Valid
    @JsonProperty("requirements")
    List<RoleRequirementRequest> requirements;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleRequirementRequest {
        
        @NotBlank(message = "Account type is required")
        @JsonProperty("accountType")
        String accountType;
        
        @JsonProperty("quantity")
        int quantity;
    }
}
