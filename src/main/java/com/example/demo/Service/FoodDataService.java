package com.example.demo.Service;

import com.example.demo.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodDataService {
    @Autowired
    private FoodRepository foodRepository;

    public List<String> getFoodDescriptions() {
        return foodRepository.findAllDescriptions();
    }
}
