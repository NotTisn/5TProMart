package com.fivetpromart.infrastructure.persistence.product.adapter;

import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import com.fivetpromart.infrastructure.persistence.product.mapper.ProductPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.product.repository.IProductJpaRepository;
import com.fivetpromart.infrastructure.persistence.product.spec.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductAdapter implements IProductRepository {

    private final ProductPersistenceMapper mapper;
    private final IProductJpaRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        // Delegate to save() - both do the same thing
        return save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        // Delegate to save() - JPA handles update if entity exists
        return save(product);
    }

    @Override
    public Product save(Product product) {
        ProductDbo dbo = mapper.toDbo(product);
        ProductDbo savedDbo = productRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public Optional<Product> findById(String productId) {
        // Use query that filters soft-deleted products
        return productRepository.findByIdAndNotDeleted(productId).map(mapper::toDomain);
    }

    @Override
    public List<Product> findByName(String productName) {
        return productRepository.findByProductNameContainingAndNotDeleted(productName)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAllNotDeleted()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByProductName(String name) {
        return productRepository.existsByProductName(name);
    }

//    @Override
//    public boolean existsById(String productId) {
//        return false;
//    }

    @Override
    public void delete(Product product) {
        // SOFT DELETE: Set deletedAt and deletedBy instead of removing from database
        ProductDbo dbo = productRepository.findById(product.getProductId())
                .orElseThrow();
        
        dbo.setDeletedAt(LocalDateTime.now());
        dbo.setDeletedBy(getCurrentUser());
        
        productRepository.save(dbo);
    }
    
    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }

    @Override
    public Page<Product> searchProducts(ProductSearchQuery query, Pageable pageable) {
        // 1. Tạo Specification từ DTO Filter
        Specification<ProductDbo> spec = ProductSpecification.getSpec(query);

        // 2. Truyền thẳng Pageable vào JPA
        // JPA tự động xử lý LIMIT, OFFSET, ORDER BY dựa trên Pageable
        Page<ProductDbo> dboPage = productRepository.findAll(spec, pageable);

        return dboPage.map(mapper::toDomain);
    }

    @Override
    public Long countAll() {
        return productRepository.countAllProducts();
    }

    @Override
    public Long countByTotalStockQuantityGreaterThan(Long threshold) {
        return productRepository.countByTotalStockQuantityGreaterThan(threshold);
    }

    @Override
    public Long countByTotalStockQuantityEquals(Long quantity) {
        return productRepository.countByTotalStockQuantityEquals(quantity);
    }
}
