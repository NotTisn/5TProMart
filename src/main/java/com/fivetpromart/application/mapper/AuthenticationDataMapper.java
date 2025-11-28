package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.AuthenticationTokensDto;
import com.fivetpromart.domain.model.AuthenticationTokens;
import com.fivetpromart.domain.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationDataMapper {
    AuthenticationTokensDto toDto(AuthenticationTokens domain);
}
