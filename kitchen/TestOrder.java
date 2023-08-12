package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.ArrayList;

public class TestOrder extends Order {
    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    @Override
    protected void initDishes() {
        this.dishes = new ArrayList<>();
        Dish[] values = Dish.values();
        int testCounter = (int) (Math.random() * 3 + 1);
        for (int i = 0; i < testCounter; i++) {
            int dishNumber = (int) (Math.random() * values.length);
            this.dishes.add(values[dishNumber]);
        }
    }
}
