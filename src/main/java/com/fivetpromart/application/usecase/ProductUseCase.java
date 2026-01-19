package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;
import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.application.mapper.ProductDataMapper;
import com.fivetpromart.application.port.in.IProductUseCasePort;
import com.fivetpromart.application.port.out.ICategoryRepository;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.domain.exception.CategoryNotFoundException;
import com.fivetpromart.domain.exception.ProductAlreadyExistsException;
import com.fivetpromart.domain.exception.ProductNotFoundException;
import com.fivetpromart.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            throw new ProductAlreadyExistsException(command.getProductName());
        }

        if (!categoryRepository.existsById(command.getCategoryId())) {
            throw new CategoryNotFoundException(command.getCategoryId());
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
                .orElseThrow(() -> new ProductNotFoundException(command.getProductId()));

        String newCategoryId = command.getCategoryId();
        String oldCategoryId = product.getCategoryId();

        if (newCategoryId != null
                && !newCategoryId.isBlank()
                && !newCategoryId.equals(oldCategoryId)) {
            if (!categoryRepository.existsById(newCategoryId)) {
                throw new CategoryNotFoundException(newCategoryId);
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
                .orElseThrow(() -> new ProductNotFoundException(productId));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public Page<ProductDto> getAllProducts(ProductSearchQuery query, Pageable pageable) {
        // Gọi Repo lấy Page Domain
        Page<Product> productPage = productRepository.searchProducts(query, pageable);

        // Map sang Page DTO
        return productPage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return mapper.toDto(product);
    }

    /**
     * Update product's total stock quantity by summing all lot quantities
     */
    @Transactional
    public void updateTotalStockQuantity(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Calculate total stock from all lots for this product
        Integer totalStock = productRepository.calculateTotalStockQuantity(productId);
        
        product.updateTotalStockQuantity(totalStock);
        productRepository.save(product);
    }
}
