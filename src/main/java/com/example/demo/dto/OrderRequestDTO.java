package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class OrderRequestDTO {
    private Integer id_user;
    private List<OrderItemDTO> items;
//    private String order_time;
}