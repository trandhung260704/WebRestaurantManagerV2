package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_discount;

    @Column(unique = true)
    private String code;

    private String name;
    private Date start_date;
    private Date end_date;

    private Integer discount_percent;
}
