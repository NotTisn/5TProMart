package com.fivetpromart.infrastructure.persistence.workshift;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shift_role_configs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftRoleConfigDbo {
    
    @Id
    @Column(name = "id")
    String id;
    
    @Column(name = "config_name", nullable = false)
    String configName;
    
    @Column(name = "description", length = 500)
    String description;
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
    
    @ElementCollection
    @CollectionTable(
            name = "role_requirements",
            joinColumns = @JoinColumn(name = "config_id")
    )
    @Builder.Default
    List<RoleRequirementDbo> requirements = new ArrayList<>();
}
