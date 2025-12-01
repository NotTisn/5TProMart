package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;

import java.util.List;

public interface IProductUseCasePort {
    ProductDto addNewProduct(ProductCreationCommand command);
    ProductDto updateProduct(ProductUpdateCommand command);
    void deleteProduct(String productId);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(String productId);
}
