package com.example.demo.Service;

import com.example.demo.repository.BillRepository;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final IngredientRepository ingredientRepository;
    private final BillRepository billRepository;
    private final OrderDetailRepository orderDetailRepository;

    public BigDecimal getTotalRevenue() {
        return billRepository.getTotalRevenue() != null ? billRepository.getTotalRevenue() : BigDecimal.ZERO;
    }

    public BigDecimal getTotalIngredientCost() {
        return ingredientRepository.getTotalIngredientCost() != null ? ingredientRepository.getTotalIngredientCost() : BigDecimal.ZERO;
    }

    public BigDecimal getNetProfit() {
        return getTotalRevenue().subtract(getTotalIngredientCost());
    }

    public Map<String, Long> getTop5BestSellingFoods() {
        List<Object[]> results = orderDetailRepository.findTop5BestSellingFoods();
        Map<String, Long> topFoods = new LinkedHashMap<>();

        for (Object[] row : results) {
            String foodName = (String) row[0];
            Long totalSold = ((Number) row[1]).longValue();
            topFoods.put(foodName, totalSold);
        }

        return topFoods;
    }
}
