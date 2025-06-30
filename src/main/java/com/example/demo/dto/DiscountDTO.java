package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DiscountDTO {
    private Integer id_discount;
    private String code;
    private String name;
    private Date start_date;
    private Date end_date;
    private Integer discount_percent;
}
