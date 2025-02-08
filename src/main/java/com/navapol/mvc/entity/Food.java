package com.navapol.mvc.entity;

import java.time.LocalDate;

public abstract class Food {
    private String id;
    private FoodType type;
    private LocalDate expiryDate;

    public Food(String id, FoodType type, LocalDate expiryDate) {
        this.id = id;
        this.type = type;
        this.expiryDate = expiryDate;
    }

    public String getId() {
        return id;
    }

    public FoodType getType() {
        return type;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public abstract boolean isExpired();
}