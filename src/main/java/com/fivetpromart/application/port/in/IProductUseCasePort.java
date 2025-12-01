package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCommand;

import java.util.List;

public interface IProductUseCasePort {
    ProductDto addNewProduct(ProductCommand command);
    ProductDto updateProduct(ProductCommand command);
    void deleteProduct(String productId);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(String productId);
}
