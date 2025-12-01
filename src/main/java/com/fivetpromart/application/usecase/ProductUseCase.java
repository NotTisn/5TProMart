package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCommand;
import com.fivetpromart.application.mapper.ProductDataMapper;
import com.fivetpromart.application.port.in.IProductUseCasePort;
import com.fivetpromart.application.port.out.ICategoryRepository;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.infrastructure.error.AppException;
import com.fivetpromart.infrastructure.error.ErrorCode;
import com.fivetpromart.infrastructure.persistence.product.adapter.ProductAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductUseCase implements IProductUseCasePort {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final ProductDataMapper mapper;

    @Override
    @Transactional
    public ProductDto addNewProduct(ProductCommand command) {

        if (productRepository.existsByProductName(command.getProductName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        if (!categoryRepository.existsById(command.getCategoryId())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Product product = Product.createProduct(
                command.getProductName(),
                command.getCategoryId(),
                command.getUnitOfMeasure(),
                command.getSellingPrice()
        );

        return mapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductDto updateProduct(ProductCommand command) {
        //TODO: implement here
        return null;
    }

    @Override
    public void deleteProduct(String productId) {
        //TODO: implement here
    }

    @Override
    public List<ProductDto> getAllProducts() {
        //TODO: implement here
        return List.of();
    }

    @Override
    public ProductDto getProductById(String productId) {
        //TODO: implement here
        return null;
    }
}
