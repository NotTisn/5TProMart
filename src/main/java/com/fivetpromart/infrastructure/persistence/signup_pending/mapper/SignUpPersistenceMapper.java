package com.fivetpromart.infrastructure.persistence.signup_pending.mapper;

import com.fivetpromart.application.dto.SignUpRequestDto;
import com.fivetpromart.infrastructure.persistence.signup_pending.SignUpRequestDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpPersistenceMapper {
    SignUpRequestDbo toDbo(SignUpRequestDto dto);
    SignUpRequestDto toDto(SignUpRequestDbo dbo);
}