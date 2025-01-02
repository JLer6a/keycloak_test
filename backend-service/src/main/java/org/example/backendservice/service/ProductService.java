package org.example.backendservice.service;


import org.example.backendservice.entity.Product;

import java.util.Optional;

public interface ProductService {

    Iterable<Product> findAllProducts(String filter);

    Product createProduct(String title, String details, String name);

    Optional<Product> findProduct(int productId);

    void updateProduct(Integer id, String title, String details);

    void deleteProduct(Integer id);

    Iterable<Product> findProductsByUserAndTitle(String username, String filter);
}
