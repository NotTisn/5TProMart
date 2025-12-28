package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;
import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.presentation.dto.request.StockInventoryRequest;
import com.fivetpromart.presentation.dto.response.StockInventoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockInventoryPresentationMapper {
    StockInventoryCreationCommand toCommand(StockInventoryRequest stockInventory);
    //StockInventoryUpdateCommand toUpdateCommand(StockInventoryRequest stockInventory);

    StockInventoryResponse toResponse(StockInventoryDto stockInventory);

}
