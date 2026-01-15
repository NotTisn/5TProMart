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
    
    // Map request to creation command
    StockInventoryCreationCommand toCommand(StockInventoryRequest request);
    
    StockInventoryCreationCommand toCreationCommand(StockInventoryRequest request);
    
    // Map update request to update command
    StockInventoryUpdateCommand toUpdateCommand(StockInventoryUpdateRequest request);

    // Map DTO to response
    StockInventoryResponse toResponse(StockInventoryDto stockInventory);

    // Map search params to query
    default StockInventorySearchQuery toSearchQuery(String search, String productId, 
                                                     String status, String sortBy, String order) {
        return StockInventorySearchQuery.builder()
                .search(search)
                .productId(productId)
                .status(status)
                .sortBy(sortBy)
                .order(order)
                .build();
    }
}
