package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;
import com.fivetpromart.application.mapper.ProductDataMapper;
import com.fivetpromart.application.port.in.IProductUseCasePort;
import com.fivetpromart.application.port.out.ICategoryRepository;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.infrastructure.error.AppException;
import com.fivetpromart.infrastructure.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ProductDto addNewProduct(ProductCreationCommand command) {

        if (productRepository.existsByProductName(command.getProductName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        if (!categoryRepository.existsById(command.getCategoryId())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Product product = Product.create(
                command.getProductName(),
                command.getCategoryId(),
                command.getUnitOfMeasure(),
                command.getSellingPrice()
        );

        return mapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductUpdateCommand command) {
        Product product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        String newCategoryId = command.getCategoryId();
        String oldCategoryId = product.getCategoryId();

        if (newCategoryId != null
                && !newCategoryId.isBlank()
                && !newCategoryId.equals(oldCategoryId)) {
            if (!categoryRepository.existsById(newCategoryId)) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }
        }

        product.updateProduct(
                command.getProductName(),
                command.getCategoryId(),
                command.getUnitOfMeasure(),
                command.getSellingPrice()
        );

        return mapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productRepository.delete(product);
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
