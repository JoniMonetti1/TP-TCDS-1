package com.example.tptallerconstruccionsoftware.services;

import com.example.tptallerconstruccionsoftware.dto.ProductRequest;
import com.example.tptallerconstruccionsoftware.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductResponse> getProducts(Pageable pageable);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getProductsByName(String name);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
