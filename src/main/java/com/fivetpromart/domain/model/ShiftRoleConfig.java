package com.fivetpromart.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftRoleConfig {
    
    String id;
    String configName;
    String description;
    boolean isActive;
    
    @Builder.Default
    List<RoleRequirement> requirements = new ArrayList<>();
    
    // Factory method for creating new config
    public static ShiftRoleConfig create(
            String id,
            String configName,
            String description,
            List<RoleRequirement> requirements
    ) {
        return ShiftRoleConfig.builder()
                .id(id)
                .configName(configName)
                .description(description)
                .isActive(true)
                .requirements(requirements != null ? new ArrayList<>(requirements) : new ArrayList<>())
                .build();
    }
    
    // Factory method for reconstitution from database
    public static ShiftRoleConfig reconstitute(
            String id,
            String configName,
            String description,
            boolean isActive,
            List<RoleRequirement> requirements
    ) {
        return ShiftRoleConfig.builder()
                .id(id)
                .configName(configName)
                .description(description)
                .isActive(isActive)
                .requirements(requirements != null ? new ArrayList<>(requirements) : new ArrayList<>())
                .build();
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void activate() {
        this.isActive = true;
    }
    
    // Nested value object for role requirements
    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleRequirement {
        String accountType;
        int quantity;
        
        public static RoleRequirement of(String accountType, int quantity) {
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity must be non-negative");
            }
            return new RoleRequirement(accountType, quantity);
        }
    }
}
