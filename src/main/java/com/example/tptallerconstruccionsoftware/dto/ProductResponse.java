package com.example.tptallerconstruccionsoftware.dto;

import com.example.tptallerconstruccionsoftware.models.Product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String nombre, String descripcion, BigDecimal precio) {

    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(product.getId(), product.getNombre(), product.getDescripcion(), product.getPrecio());
    }
}
