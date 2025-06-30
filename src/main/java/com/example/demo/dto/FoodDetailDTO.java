package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FoodDetailDTO {
    private Integer id_food_detail;
    private Integer id_food;
    private Integer id_ingredient;
    private BigDecimal quantity_used;
}