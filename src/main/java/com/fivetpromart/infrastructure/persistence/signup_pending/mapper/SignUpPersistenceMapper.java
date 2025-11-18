package com.fivetpromart.infrastructure.persistence.signup_pending.mapper;

import com.fivetpromart.application.dto.SignUpRequestDto;
import com.fivetpromart.domain.model.SignUpRequest;
import com.fivetpromart.infrastructure.persistence.signup_pending.SignUpRequestDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpPersistenceMapper {
    SignUpRequest toDomain(SignUpRequestDbo dbo);
    SignUpRequestDbo toDbo(SignUpRequest domain);
}