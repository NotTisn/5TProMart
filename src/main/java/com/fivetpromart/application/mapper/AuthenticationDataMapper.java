package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.AuthenticationTokensDto;
import com.fivetpromart.domain.model.AuthenticationTokens;
import com.fivetpromart.domain.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthenticationDataMapper {
    AuthenticationTokensDto toDto(AuthenticationTokens domain);
}
