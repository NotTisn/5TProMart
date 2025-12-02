package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDataMapper {
    ProductDto toDto(Product domain);
}
