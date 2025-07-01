package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class OrderItemDTO {
    private Integer id_food;
    private Integer quantity;
}

