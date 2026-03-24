package com.example.urbanvibe.ecommerce;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data // Generar getters y setters
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 300)
    private String description;

    @Column
    private String imageUrl;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private int stock;

    public Product () {}
}
