package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    Product addProduct(Product product);
    Product updateProduct(Product product);
    Product save(Product product);
    Optional<Product> findById(String productId);
    List<Product> findByName(String productName);
    List<Product> findAll();
    boolean existsByProductName(String name);
    void delete(Product product);
}
