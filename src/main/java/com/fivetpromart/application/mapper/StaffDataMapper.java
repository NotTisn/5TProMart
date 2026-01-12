package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.StaffAccountDto;
import com.fivetpromart.domain.model.Staff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffDataMapper {
    StaffAccountDto toDto(Staff domain);
}
