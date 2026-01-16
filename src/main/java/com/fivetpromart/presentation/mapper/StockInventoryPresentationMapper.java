package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;
import com.fivetpromart.application.dto.command.StockInventoryUpdateCommand;
import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.presentation.dto.request.StockInventoryRequest;
import com.fivetpromart.presentation.dto.request.StockInventoryUpdateRequest;
import com.fivetpromart.presentation.dto.response.StockInventoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockInventoryPresentationMapper {
    StockInventoryCreationCommand toCommand(StockInventoryRequest stockInventory);
    
    StockInventoryCreationCommand toCreationCommand(StockInventoryRequest stockInventory);
    
    StockInventoryUpdateCommand toUpdateCommand(StockInventoryUpdateRequest stockInventory);

    StockInventoryResponse toResponse(StockInventoryDto stockInventory);
    
    /**
     * Map search parameters to query object
     */
    default StockInventorySearchQuery toSearchQuery(String search, String productId, String status, String sortBy, String order) {
        return StockInventorySearchQuery.builder()
                .search(search)
                .productId(productId)
                .status(status)
                .sortBy(sortBy)
                .order(order)
                .build();
    }
}
