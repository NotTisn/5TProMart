package com.fivetpromart.infrastructure.persistence.workshift;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequirementDbo {
    
    @Column(name = "account_type")
    String accountType;
    
    @Column(name = "quantity")
    int quantity;
}
