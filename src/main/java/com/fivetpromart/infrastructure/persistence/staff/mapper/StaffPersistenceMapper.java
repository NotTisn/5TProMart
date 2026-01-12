package com.fivetpromart.infrastructure.persistence.staff.mapper;

import com.fivetpromart.domain.model.Staff;
import com.fivetpromart.infrastructure.persistence.staff.StaffDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffPersistenceMapper {
    
    default StaffDbo toDbo(Staff domain) {
        if (domain == null) return null;

        return StaffDbo.builder()
                .profileId(domain.getProfileId())
                .userId(domain.getUserId())
                .username(domain.getUsername())
                .fullName(domain.getFullName())
                .email(domain.getEmail())
                .phoneNumber(domain.getPhoneNumber())
                .dateOfBirth(domain.getDateOfBirth())
                .accountType(domain.getAccountType())
                .avatarUrl(domain.getAvatarUrl())
                .location(domain.getLocation())
                .bio(domain.getBio())
                .build();
    }

    default Staff toDomain(StaffDbo dbo) {
        if (dbo == null) return null;

        return Staff.reconstitute(
                dbo.getProfileId(),
                dbo.getUserId(),
                dbo.getUsername(),
                dbo.getFullName(),
                dbo.getEmail(),
                dbo.getPhoneNumber(),
                dbo.getDateOfBirth(),
                dbo.getAccountType(),
                dbo.getAvatarUrl(),
                dbo.getLocation(),
                dbo.getBio()
        );
    }
}
