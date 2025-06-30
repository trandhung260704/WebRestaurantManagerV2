package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class BillDTO {
    private Integer id_bill;
    private Integer id_order;
    private Integer id_discount;
    private BigDecimal total_price;
    private String payment_method;
    private Timestamp bill_time;
}