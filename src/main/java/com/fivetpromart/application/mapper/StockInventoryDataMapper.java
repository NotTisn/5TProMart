package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.domain.model.StockInventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockInventoryDataMapper {
    StockInventoryDto toDto(StockInventory domain);
}
