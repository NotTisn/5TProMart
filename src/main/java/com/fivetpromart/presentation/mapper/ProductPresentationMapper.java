package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.presentation.dto.request.ProductRequest;
import com.fivetpromart.presentation.dto.response.ProductResponse;

public interface ProductPresentationMapper {
    ProductCreationCommand toCommand(ProductRequest product);
    ProductResponse toProductResponse(ProductDto product);
}
