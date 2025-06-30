package com.example.demo.controller;

import com.example.demo.entity.Food;
import com.example.demo.entity.Ingredient;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final FoodRepository foodRepo;
    private final IngredientRepository ingredientRepo;

    @GetMapping("/foods")
    public List<Food> searchFoods(@RequestParam String keyword) {
        return foodRepo.findByNameContainingIgnoreCase(keyword);
    }

    @GetMapping("/ingredients")
    public List<Ingredient> searchIngredients(@RequestParam String keyword) {
        return ingredientRepo.findByNameContainingIgnoreCase(keyword);
    }
}
