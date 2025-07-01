package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class BillResponseDTO {
    private Integer id_bill;
    private BigDecimal total_price;
    private String payment_method;
    private Timestamp bill_time;
    private Integer id_discount;
    private OrderDTO order;

    @Data
    public static class OrderDTO {
        private Integer id_order;
    }
}
