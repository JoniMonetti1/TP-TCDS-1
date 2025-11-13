package com.example.tptallerconstruccionsoftware.services;

import com.example.tptallerconstruccionsoftware.dto.ProductRequest;
import com.example.tptallerconstruccionsoftware.dto.ProductResponse;
import com.example.tptallerconstruccionsoftware.exceptions.ProductAlreadyExistsException;
import com.example.tptallerconstruccionsoftware.exceptions.ProductNotFoundException;
import com.example.tptallerconstruccionsoftware.models.Product;
import com.example.tptallerconstruccionsoftware.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponse::fromEntity);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return ProductResponse.fromEntity(findByIdOrThrow(id));
    }

    @Override
    public List<ProductResponse> getProductsByName(String name) {
        return productRepository.findByNombreIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new ProductAlreadyExistsException(request.nombre());
        }

        Product product = new Product();
        product.setNombre(request.nombre());
        product.setDescripcion(request.descripcion());
        product.setPrecio(request.precio());

        return ProductResponse.fromEntity(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findByIdOrThrow(id);

        if (productRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre(), id)) {
            throw new ProductAlreadyExistsException(request.nombre());
        }

        product.setNombre(request.nombre());
        product.setDescripcion(request.descripcion());
        product.setPrecio(request.precio());

        return ProductResponse.fromEntity(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findByIdOrThrow(id);
        productRepository.delete(product);
    }

    private Product findByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
