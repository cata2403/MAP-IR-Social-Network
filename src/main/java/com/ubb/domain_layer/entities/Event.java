package com.ubb.domain_layer.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Event extends Entity<Long> {

    private final String eventName;
    private final List<User> observers = new ArrayList<>();
    private int minFlockSize;

    public Event(Long id,  String eventName) {
        super(id);
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setMinFlockSize(int minFlockSize) {
        this.minFlockSize = minFlockSize;
    }

    public int getMinFlockSize() {
        return minFlockSize;
    }

    public void addObserver(User observer) {
        observers.add(observer);
    }

    public void removeObserver(User observer) {
        observers.remove(observer);
    }

    public List<User> getObservers() {
        return observers;
    }

    public void notifyObservers(){
        for (User observer : observers) {
            observer.react();
        }
    }

    public abstract Double startEvent();
}


