package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;
import com.fivetpromart.presentation.dto.request.ProductRequest;
import com.fivetpromart.presentation.dto.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductPresentationMapper {
    ProductCreationCommand toCommand(ProductRequest product);
    ProductUpdateCommand toUpdateCommand(ProductRequest product);
    ProductResponse toProductResponse(ProductDto product);
}
