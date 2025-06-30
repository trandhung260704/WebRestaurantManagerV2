package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class OrdersDTO {
    private Integer id_order;
    private Integer id_user;
    private Timestamp order_time;
    private BigDecimal total_price;
    private String status;
}
