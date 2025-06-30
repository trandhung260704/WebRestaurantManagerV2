package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_bill;

    @OneToOne
    @JoinColumn(name = "id_order")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "id_discount")
    private Discount discount;

    private BigDecimal total_price;
    private String payment_method;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp bill_time;
}
