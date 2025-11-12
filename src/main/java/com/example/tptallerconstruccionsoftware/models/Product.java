package com.example.tptallerconstruccionsoftware.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="productos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false, length=120)
    private String nombre;

    @Column(nullable=false, length=255)
    private String descripcion;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal precio;
}
