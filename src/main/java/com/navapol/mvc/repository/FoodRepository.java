package com.navapol.mvc.repository;

import java.util.List;
import java.util.Optional;

import com.navapol.mvc.entity.Food;

public interface FoodRepository {
    List<Food> findAll();
    Optional<Food> findById(String id);
    void save(Food food);
}
