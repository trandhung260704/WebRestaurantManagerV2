package com.example.demo.repository;

import com.example.demo.entity.Food;
import com.example.demo.entity.FoodDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByNameContainingIgnoreCase(String name);
}
