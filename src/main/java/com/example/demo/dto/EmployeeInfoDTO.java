package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmployeeInfoDTO {
    private Integer id_employee;
    private Integer id_user;
    private String position;
    private BigDecimal salary;
    private String start_date;
}