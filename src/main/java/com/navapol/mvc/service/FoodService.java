package com.navapol.mvc.service;

import com.navapol.mvc.entity.Food;
import com.navapol.mvc.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Optional<Food> getFoodById(String id) {
        return foodRepository.findById(id);
    }

    public String checkFoodExpiry(String id) {
        Optional<Food> foodOpt = foodRepository.findById(id);
        if (foodOpt.isPresent()) {
            Food food = foodOpt.get();
            return food.isExpired() ? "Expired" : "Not Expired";
        }
        return "Food not found";
    }

    public String getSummary() {
        List<Food> foods = foodRepository.findAll();
        int freshExpired = 0, freshGood = 0;
        int pickledExpired = 0, pickledGood = 0;
        int cannedExpired = 0, cannedGood = 0;
    
        for (Food food : foods) {
            if (food.getType() == null) {
                continue; // ข้ามการประมวลผลถ้า type เป็น null
            }
            switch (food.getType()) {
                case FRESH:
                    if (food.isExpired()) freshExpired++; else freshGood++;
                    break;
                case PICKLED:
                    if (food.isExpired()) pickledExpired++; else pickledGood++;
                    break;
                case CANNED:
                    if (food.isExpired()) cannedExpired++; else cannedGood++;
                    break;
            }
        }
    
        return String.format("Fresh: Expired=%d, Good=%d\nPickled: Expired=%d, Good=%d\nCanned: Expired=%d, Good=%d",
                freshExpired, freshGood, pickledExpired, pickledGood, cannedExpired, cannedGood);
    }
}