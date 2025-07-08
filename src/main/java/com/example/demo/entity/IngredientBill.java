package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "ingredient_bill")
public class IngredientBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_ingredient_bill;

    @ManyToOne
    @JoinColumn(name = "id_ingredient", referencedColumnName = "id_ingredient", foreignKey = @ForeignKey(name = "fk_ingredient_bill_ingredient"))
    private Ingredient ingredient;

    private BigDecimal quantity;

    private BigDecimal unit_price;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp import_time;

    private String note;
}
