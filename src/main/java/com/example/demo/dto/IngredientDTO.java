package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class IngredientDTO {
    private Integer id_ingredient;
    private String name;
    private BigDecimal quantity;
    private BigDecimal unit_price;
    private String unit;
    private String origin;
    private Timestamp created_at;
}