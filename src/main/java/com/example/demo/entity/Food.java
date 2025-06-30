package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_food;

    private String name;
    private BigDecimal price;
    private String category;

    @Column(columnDefinition = "TEXT")
    private String image_url;

    private String description;
    private String status;

    // Getters and setters
}
