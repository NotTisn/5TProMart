package com.fivetpromart.infrastructure.persistence.product.adapter;

import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import com.fivetpromart.infrastructure.persistence.product.mapper.ProductPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.product.repository.IProductJpaRepository;
import lombok.RequiredArgsConstructor;
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
        return Optional.empty();
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
}
