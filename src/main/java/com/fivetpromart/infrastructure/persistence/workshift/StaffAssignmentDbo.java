package com.fivetpromart.infrastructure.persistence.workshift;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffAssignmentDbo {
    
    @Column(name = "profile_id")
    String profileId;
    
    @Column(name = "full_name")
    String fullName;
    
    @Column(name = "account_type")
    String accountType;
    
    @Column(name = "email")
    String email;
    
    @Column(name = "phone_number")
    String phoneNumber;
    
    @Column(name = "status")
    String status;
}
