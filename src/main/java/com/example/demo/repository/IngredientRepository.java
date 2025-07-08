package com.example.demo.repository;

import com.example.demo.entity.Ingredient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    Page<Ingredient> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT i FROM Ingredient i WHERE i.quantity <= :threshold")
    List<Ingredient> findLowStockIngredients(@Param("threshold") BigDecimal threshold);

}
