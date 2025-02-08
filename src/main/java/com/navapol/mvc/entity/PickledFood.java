package com.navapol.mvc.entity;

import java.time.LocalDate;

public class PickledFood extends Food{
    public PickledFood(String id, LocalDate expiryDate) {
        super(id, FoodType.PICKLED, expiryDate);
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().getMonthValue() > getExpiryDate().getMonthValue() &&
               LocalDate.now().getYear() >= getExpiryDate().getYear();
    }
}
