package com.fivetpromart.infrastructure.persistence.profile.mapper;

import com.fivetpromart.domain.model.Profile;
import com.fivetpromart.infrastructure.persistence.profile.ProfileDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfilePersistenceMapper {

    // 1. Dịch từ Domain -> DBO
    // Domain có getFullName(), nhưng DBO không có field này -> MapStruct tự lờ đi (hoặc warning)
    // Tốt nhất nên ignore explicit để code sạch warning
    //@Mapping(target = "createdDate", ignore = true) // Ví dụ trường audit
    ProfileDbo toDbo(Profile domainEntity);

    // 2. Dịch từ DBO -> Domain
    // KHÔNG CẦN @AfterMapping để tính fullName nữa!
    // Vì khi map firstName/lastName vào Constructor của Profile,
    // method profile.getFullName() sẽ tự tính đúng.
    Profile toDomain(ProfileDbo dbo);
}