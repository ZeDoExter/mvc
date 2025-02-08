package com.navapol.mvc.repository;

import com.navapol.mvc.entity.*;

import jakarta.annotation.PostConstruct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Repository
public class FoodRepositoryImpl implements FoodRepository {
    private static final String DB_FILE = "foods.json";
    private final List<Food> foods = new ArrayList<>();

    private final RuntimeTypeAdapterFactory<Food> foodAdapterFactory = RuntimeTypeAdapterFactory
            .of(Food.class, "type")
            .registerSubtype(FreshFood.class, "FRESH")
            .registerSubtype(PickledFood.class, "PICKLED")
            .registerSubtype(CannedFood.class, "CANNED"); 

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(foodAdapterFactory)
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create(); 
    

    private final Type foodListType = new TypeToken<List<Food>>() {}.getType();
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        loadFromFile();
        if (foods.isEmpty()) {
            generateRandomFoods(50);
        }
    }

    private void loadFromFile() {
        try (FileReader reader = new FileReader(DB_FILE)) {
            List<Food> loadedFoods = gson.fromJson(reader, foodListType);
            if (loadedFoods != null) {
                foods.clear();
                foods.addAll(loadedFoods);
            }
        } catch (IOException e) {
            System.out.println("No existing database, starting fresh");
        }
    }

    //generate random foods
    

    private void generateRandomFoods(int count) {
        for (int i = 0; i < count; i++) {
            String id = generateRandomId();
            FoodType type = FoodType.values()[random.nextInt(FoodType.values().length)];
            LocalDate expiryDate = generateRandomExpiryDate();
            Food food = createFood(id, type, expiryDate);
            foods.add(food);
        }
        saveToFile();
    }

    private String generateRandomId() {
        return String.valueOf(random.nextInt(9) + 1) + String.format("%05d", random.nextInt(100000));
    }

    private LocalDate generateRandomExpiryDate() {
        int year = 2015 + random.nextInt(16);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28);
        return LocalDate.of(year, month, day);
    }

    private Food createFood(String id, FoodType type, LocalDate expiryDate) {
        switch (type) {
            case FRESH:
                return new FreshFood(id, expiryDate);
            case PICKLED:
                return new PickledFood(id, expiryDate);
            case CANNED:
                return new CannedFood(id, expiryDate);
            default:
                throw new IllegalArgumentException("Invalid food type: " + type);
        }
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(DB_FILE)) {
            gson.toJson(foods, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //implement methods from FoodRepository

    @Override
    public List<Food> findAll() {
        return new ArrayList<>(foods);
    }

    @Override
    public Optional<Food> findById(String id) {
        return foods.stream()
                .filter(food -> food.getId().equals(id))
                .findFirst();
    }

    @Override
    public void save(Food food) {
        foods.add(food);
        saveToFile();
    }
}