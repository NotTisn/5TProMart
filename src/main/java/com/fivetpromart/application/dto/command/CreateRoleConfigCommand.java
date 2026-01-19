package com.fivetpromart.application.dto.command;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoleConfigCommand {
    
    String configName;
    String description;
    List<RoleRequirementCommand> requirements;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleRequirementCommand {
        String accountType;
        int quantity;
    }
}
