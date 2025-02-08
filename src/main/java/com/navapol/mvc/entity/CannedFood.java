package com.navapol.mvc.entity;

import java.time.LocalDate;

public class CannedFood extends Food {
    public CannedFood(String id, LocalDate expiryDate) {
        super(id, FoodType.CANNED, expiryDate);
    }

    @Override
    public boolean isExpired() {
        LocalDate extendedExpiryDate = getExpiryDate().plusMonths(9);
        return LocalDate.now().isAfter(extendedExpiryDate);
    }
}