package com.example.demo.repository;

import com.example.demo.entity.Food;
import com.example.demo.entity.FoodDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodDetailRepository extends JpaRepository<FoodDetail, Integer> {
    List<FoodDetail> findByFood(Food food);
}
