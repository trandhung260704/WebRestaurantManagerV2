package com.example.demo.repository;

import com.example.demo.entity.IngredientBill;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IngredientBillRepository extends JpaRepository<IngredientBill, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM IngredientBill b WHERE b.ingredient.id_ingredient = :id")
    void deleteByIngredientId(Integer id);

    @Query("SELECT SUM(b.quantity * b.unit_price) FROM IngredientBill b")
    BigDecimal getTotalIngredientCost();
}

