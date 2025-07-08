package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class IngredientBillDTO {
    private Integer id_ingredient_bill;
    private Integer id_ingredient;
    private String name;
    private BigDecimal quantity;
    private BigDecimal unit_price;
    private Timestamp import_time;
    private String note;
}
