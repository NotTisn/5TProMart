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
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductAdapter implements IProductRepository {

    private final ProductPersistenceMapper mapper;
    private final IProductJpaRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(Product product) {
        return null;
    }

    @Override
    public Product save(Product product) {
        ProductDbo dbo = mapper.toDbo(product);
        ProductDbo savedDbo = productRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public Optional<Product> findById(String productId) {
        return productRepository.findById(productId).map(mapper::toDomain);
    }

    @Override
    public List<Product> findByName(String productName) {
        return List.of();
    }

    @Override
    public List<Product> findAll() {
        return List.of();
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
    public Integer calculateTotalStockQuantity(String productId) {
        // Sum all lot quantities for this product
        Integer total = productRepository.calculateTotalStockQuantity(productId);
        return total != null ? total : 0;
    }
}
