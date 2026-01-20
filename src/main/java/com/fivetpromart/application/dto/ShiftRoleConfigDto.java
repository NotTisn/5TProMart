package com.fivetpromart.application.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftRoleConfigDto {
    
    String id;
    String configName;
    String description;
    boolean isActive;
    
    @Builder.Default
    List<RoleRequirementDto> requirements = new ArrayList<>();
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleRequirementDto {
        String accountType;
        int quantity;
    }
}
