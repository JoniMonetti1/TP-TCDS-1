package com.example.tptallerconstruccionsoftware.services;

import com.example.tptallerconstruccionsoftware.models.Product;
import com.example.tptallerconstruccionsoftware.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements  ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<Product> getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Override
    public ResponseEntity<List<Product>> getProductByName(String name) {
        var listOfProducts = productRepository.findByNombreIgnoreCaseContaining(name);
        return listOfProducts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(listOfProducts);
    }

    @Override
    public ResponseEntity<Product> createProduct(Product product) {
        if (product != null) {
            var savedProduct = productRepository.save(product);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(product.getId())
                    .toUri();
            return ResponseEntity.created(location).body(savedProduct);
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<Product> updateProduct(Long id, Product product) {
        var productToUpdate = productRepository.findById(id);

        if (productToUpdate.isPresent()) {
            product.setId(id);
            var updatedProductLoaded = productRepository.save(product);
            return ResponseEntity.ok(updatedProductLoaded);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        var optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
