package com.example.demo.Service;

import com.example.demo.repository.BillRepository;
import com.example.demo.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final IngredientRepository ingredientRepository;
    private final BillRepository billRepository;

    public BigDecimal getTotalRevenue() {
        return billRepository.getTotalRevenue() != null ? billRepository.getTotalRevenue() : BigDecimal.ZERO;
    }

    public BigDecimal getTotalIngredientCost() {
        return ingredientRepository.getTotalIngredientCost() != null ? ingredientRepository.getTotalIngredientCost() : BigDecimal.ZERO;
    }

    public BigDecimal getNetProfit() {
        return getTotalRevenue().subtract(getTotalIngredientCost());
    }
}
