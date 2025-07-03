package com.example.demo.controller;

import com.example.demo.entity.Food;
import com.example.demo.entity.Ingredient;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final FoodRepository foodRepo;
    private final IngredientRepository ingredientRepo;

    @GetMapping("/foods")
    public ResponseEntity<Page<Food>> searchFoods(@RequestParam(defaultValue = "") String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size, Pageable pageable) {
        Pageable pageablefood = PageRequest.of(page, size);
        return ResponseEntity.ok(foodRepo.findByNameContainingIgnoreCase(keyword,pageablefood));
    }

    @GetMapping("/ingredients")
    public ResponseEntity<Page<Ingredient>> searchIngredients(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ingredientRepo.findByNameContainingIgnoreCase(keyword, pageable));
    }
}
