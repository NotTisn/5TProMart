package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCommand;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.presentation.dto.request.ProductRequest;
import com.fivetpromart.presentation.dto.response.ProductResponse;

public interface ProductPresentationMapper {
    ProductCommand toCommand(ProductRequest product);
    ProductResponse toProductResponse(ProductDto product);
}
