package com.example.tptallerconstruccionsoftware.services;

import com.example.tptallerconstruccionsoftware.models.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<List<Product>> getProducts();
    ResponseEntity<Product> getProductById(Long id);
    ResponseEntity<List<Product>> getProductByName(String name);
    ResponseEntity<Product> createProduct(Product product);
    ResponseEntity<Product> updateProduct(Long id, Product product);
    ResponseEntity<?> deleteProduct(Long id);
}
