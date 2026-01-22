package com.fivetpromart.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInventoryUpdateCommand {
    private Long stockQuantity;
    private Long quantityShelf;
    private Long quantityStorage;
    private String status;
}
