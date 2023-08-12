package com.javarush.task.task27.task2712.kitchen;

import java.util.Observable;
import java.util.Observer;

import static com.javarush.task.task27.task2712.ConsoleHelper.writeMessage;

public class Waiter implements Observer {

    @Override
    public void update(Observable cook, Object order) {
        writeMessage(order + " was cooked by " + cook);
    }
}
