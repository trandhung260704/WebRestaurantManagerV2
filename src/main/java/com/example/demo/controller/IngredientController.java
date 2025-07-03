package com.example.demo.controller;

import com.example.demo.dto.IngredientDTO;
import com.example.demo.entity.Ingredient;
import com.example.demo.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<Page<Ingredient>> getAllIngredients(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ingredientRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Integer id) {
        return ingredientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('EMPLOYEE')")
    @GetMapping("/search")
    public ResponseEntity<Page<Ingredient>> searchByName(@RequestParam(defaultValue = "") String name,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ingredientRepository.findByNameContainingIgnoreCase(name, pageable));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Ingredient>> getLowStockIngredients(@RequestParam(defaultValue = "10") BigDecimal threshold) {
        return ResponseEntity.ok(ingredientRepository.findLowStockIngredients(threshold));
    }

    @PreAuthorize("hasRole('MANAGER' or hasRole('EMPLOYEE'))")
    @PostMapping
    public ResponseEntity<Ingredient> createIngredient(@RequestBody IngredientDTO dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setQuantity(dto.getQuantity());
        ingredient.setUnit_price(dto.getUnit_price());
        ingredient.setUnit(dto.getUnit());
        ingredient.setOrigin(dto.getOrigin());
        ingredient.setCreated_at(new Timestamp(new Date().getTime()));
        return ResponseEntity.ok(ingredientRepository.save(ingredient));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable Integer id, @RequestBody IngredientDTO dto) {
        return ingredientRepository.findById(id).map(ingredient -> {
            ingredient.setName(dto.getName());
            ingredient.setQuantity(dto.getQuantity());
            ingredient.setUnit_price(dto.getUnit_price());
            ingredient.setUnit(dto.getUnit());
            ingredient.setOrigin(dto.getOrigin());
            return ResponseEntity.ok(ingredientRepository.save(ingredient));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Integer id) {
        if (!ingredientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ingredientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
