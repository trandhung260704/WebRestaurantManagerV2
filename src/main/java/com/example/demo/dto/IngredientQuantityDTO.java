package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class IngredientQuantityDTO {
    private Integer id_ingredient;
    private BigDecimal quantity_used;
}