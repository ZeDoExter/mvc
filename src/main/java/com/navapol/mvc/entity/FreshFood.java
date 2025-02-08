package com.navapol.mvc.entity;

import java.time.LocalDate;

public class FreshFood extends Food {

    public FreshFood(String id , LocalDate expiryDate) {
        super(id, FoodType.FRESH, expiryDate);
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(getExpiryDate());
    }
    
}