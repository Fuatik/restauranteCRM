package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.List;

import static com.javarush.task.task27.task2712.ConsoleHelper.getAllDishesForOrder;
import static com.javarush.task.task27.task2712.ConsoleHelper.writeMessage;

public class Order {
    private final Tablet tablet;
    protected List<Dish> dishes;

    public Order(Tablet tablet) throws IOException {
        this.tablet = tablet;
        initDishes();
        writeMessage(toString());
    }

    public int getTotalCookingTime() {
        return this.dishes.stream().mapToInt(Dish::getDuration).sum();
    }

    public boolean isEmpty() {
        return dishes.isEmpty();
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public Tablet getTablet() {
        return tablet;
    }

    protected void initDishes() throws IOException {
        this.dishes = getAllDishesForOrder();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (isEmpty()) return result.toString();
        result.append("Your order: [").append(dishes.get(0));
        for (int i = 1; i < dishes.size(); i++) {
            result.append(", ").append(dishes.get(i).name());
        }
        result.append("] of ").append(tablet).append(", cooking time ")
                .append(getTotalCookingTime()).append("min");
        return result.toString();
    }
}
