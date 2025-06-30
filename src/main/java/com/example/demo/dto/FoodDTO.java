package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FoodDTO {
    private Integer id_food;
    private String name;
    private BigDecimal price;
    private String category;
    private String image_url;
    private String description;
    private String status;
}