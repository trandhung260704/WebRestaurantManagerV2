package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Data
public class CreateFoodRequestDTO {
    private String name;
    private BigDecimal price;
    private String category;
    private String image_url;
    private String description;
    private String status;

    private List<IngredientQuantityDTO> ingredients;
}