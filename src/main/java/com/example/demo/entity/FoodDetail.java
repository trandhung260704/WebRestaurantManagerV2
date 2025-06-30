package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "food_detail")
public class FoodDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_food_detail;

    @ManyToOne
    @JoinColumn(name = "id_food")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "id_ingredient")
    private Ingredient ingredient;

    private BigDecimal quantity_used;

}

