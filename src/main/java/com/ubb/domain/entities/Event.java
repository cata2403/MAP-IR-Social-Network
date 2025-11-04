package com.ubb.domain.entities;

import com.ubb.observer.Observer;
import java.util.ArrayList;
import java.util.List;

public abstract class Event extends Entity<Long> {

    private final String eventName;
    private final List<Observer> observers = new ArrayList<>();
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

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public abstract void startEvent(List<Duck> ducks);
}
