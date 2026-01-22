package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.ProductStatsDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;
import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductUseCasePort {
    ProductDto addNewProduct(ProductCreationCommand command);
    ProductDto updateProduct(ProductUpdateCommand command);
    void deleteProduct(String productId);
    ProductDto restoreProduct(String productId);
    List<ProductDto> getAllProducts();

    Page<ProductDto> getAllProducts(ProductSearchQuery query, Pageable pageable);

    ProductDto getProductById(String productId);

    /**
     * Get product and inventory statistics for dashboard
     */
    ProductStatsDto getProductStats();
}
