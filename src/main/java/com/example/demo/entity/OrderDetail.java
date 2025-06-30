package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_order_detail;

    @ManyToOne
    @JoinColumn(name = "id_order")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "id_food")
    private Food food;

    private Integer quantity;

}
