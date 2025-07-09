package com.example.demo.controller;

import com.example.demo.Service.StatisticsService;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RequiredArgsConstructor
public class StatisticsController {
    private final UsersRepository usersRepository;
    private final StatisticsService statisticService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/top-selling-foods")
    public ResponseEntity<Map<String, Long>> getTopSellingFoods() {
        return ResponseEntity.ok(statisticService.getTop5BestSellingFoods());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/revenue")
    public ResponseEntity<Map<String, BigDecimal>> getRevenueStats() {
        Map<String, BigDecimal> stats = new HashMap<>();
        stats.put("totalRevenue", statisticService.getTotalRevenue());
        stats.put("totalIngredientCost", statisticService.getTotalIngredientCost());
        stats.put("netProfit", statisticService.getNetProfit());

        return ResponseEntity.ok(stats);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/average-customer-age")
    public Double getAverageCustomerAge() {
        return usersRepository.findAverageCustomerAge();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/age-count")
    public Map<Integer, Long> getAgeCount() {
        List<Object[]> rawResult = usersRepository.findCustomerAgeCounts();
        Map<Integer, Long> ageMap = new LinkedHashMap<>();
        for (Object[] row : rawResult) {
            Integer age = ((Number) row[0]).intValue();
            Long count = ((Number) row[1]).longValue();
            ageMap.put(age, count);
        }
        return ageMap;
    }
}