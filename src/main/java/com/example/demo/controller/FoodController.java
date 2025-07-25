package com.example.demo.controller;

import com.example.demo.dto.CreateFoodRequestDTO;
import com.example.demo.dto.IngredientQuantityDTO;
import com.example.demo.entity.Food;
import com.example.demo.entity.FoodDetail;
import com.example.demo.entity.Ingredient;
import com.example.demo.repository.FoodDetailRepository;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/foods")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RequiredArgsConstructor
public class FoodController {

    private final FoodRepository foodRepository;
    private final FoodDetailRepository foodDetailRepository;
    private final IngredientRepository ingredientRepository;

    @PostMapping
    public ResponseEntity<?> createFoodWithIngredients(@RequestBody CreateFoodRequestDTO request) {
        Food food = new Food();
        food.setName(request.getName());
        food.setPrice(request.getPrice());
        food.setCategory(request.getCategory());
        food.setImage_url(request.getImage_url());
        food.setDescription(request.getDescription());
        food.setStatus(request.getStatus());

        Food savedFood = foodRepository.save(food);

        for (IngredientQuantityDTO item : request.getIngredients()) {
            Ingredient ing = ingredientRepository.findById(item.getId_ingredient()).orElse(null);
            if (ing == null) {
                return ResponseEntity.badRequest().body("Nguyên liệu không tồn tại với ID: " + item.getId_ingredient());
            }

            FoodDetail detail = new FoodDetail();
            detail.setFood(savedFood);
            detail.setIngredient(ing);
            detail.setQuantity_used(item.getQuantity_used());

            foodDetailRepository.save(detail);
        }

        return ResponseEntity.ok("Thêm món ăn thành công cùng nguyên liệu.");
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<Page<Food>> getFoodsPage(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Food> result;

        if (keyword == null || keyword.isBlank()) {
            result = foodRepository.findAll(pageable);
        } else {
            result = foodRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodById(@PathVariable Integer id) {
        return foodRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFoodByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(foodRepository.findByNameContainingIgnoreCase(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Integer id, @RequestBody Food updated) {
        return foodRepository.findById(id).map(food -> {
            food.setName(updated.getName());
            food.setPrice(updated.getPrice());
            food.setCategory(updated.getCategory());
            food.setDescription(updated.getDescription());
            food.setImage_url(updated.getImage_url());
            food.setStatus(updated.getStatus());
            foodRepository.save(food);
            return ResponseEntity.ok(food);
        }).orElse(ResponseEntity.notFound().build());
    }
}

