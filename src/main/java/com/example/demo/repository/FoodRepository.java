package com.example.demo.repository;

import com.example.demo.entity.Food;
import com.example.demo.entity.FoodDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByNameContainingIgnoreCase(String name);
    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT CONCAT(f.name, ': ', f.description) FROM Food f WHERE f.status = 'AVAILABLE'")
    List<String> findAllDescriptions();
}
