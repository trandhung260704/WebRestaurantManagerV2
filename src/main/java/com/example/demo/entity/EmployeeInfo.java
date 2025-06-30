package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "employee_info")
public class EmployeeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_employee;

    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private Users user;

    private String position;

    private BigDecimal salary;

    private Date start_date;

    // Getters and setters
}
