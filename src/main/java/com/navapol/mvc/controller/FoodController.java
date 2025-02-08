package com.navapol.mvc.controller;

import com.navapol.mvc.entity.Food;
import com.navapol.mvc.service.FoodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String foodId, Model model) {
        List<Food> foods = foodService.getAllFoods();
        model.addAttribute("foods", foods);

        if (foodId != null && !foodId.isEmpty()) {
            // ตรวจสอบเงื่อนไขต่างๆ
            if (!foodId.matches("\\d{6}")) {
                model.addAttribute("error", "รหัสอาหารต้องเป็นตัวเลข 6 หลัก");
            } else if (foodId.startsWith("0")) {
                model.addAttribute("error", "รหัสอาหารไม่สามารถขึ้นต้นด้วยเลข 0");
            } else if (!foodService.getFoodById(foodId).isPresent()) {
                model.addAttribute("error", "รหัสอาหารไม่พบในฐานข้อมูล");
            } else {
                String result = foodService.checkFoodExpiry(foodId);
                model.addAttribute("result", result);
            }
        }

        model.addAttribute("summary", foodService.getSummary());

        return "index";
    }
}