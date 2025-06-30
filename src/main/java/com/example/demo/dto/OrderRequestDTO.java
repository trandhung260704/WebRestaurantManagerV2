package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private Integer id_user;
    private List<OrderItemDTO> items;
}