package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;
import com.fivetpromart.presentation.dto.request.ProductRequest;
import com.fivetpromart.presentation.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductPresentationMapper {
    ProductCreationCommand toCommand(ProductRequest product);
    ProductUpdateCommand toUpdateCommand(ProductRequest product);
    ProductResponse toProductResponse(ProductDto product);
}
