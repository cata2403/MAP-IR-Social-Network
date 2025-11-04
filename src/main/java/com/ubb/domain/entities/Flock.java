package com.ubb.domain.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Flock<T extends Duck> extends Entity<Long>{

    private final String flockName;
    private final List<T> members = new ArrayList<>();

    public Flock(Long id, String flockName) {
        super(id);
        this.flockName = flockName;
    }

    public Double getAveragePerformance() {
        Double sum = 0.0, cnt = 0.0;
        for (Duck duck : members) {
            sum += duck.getSpeed();
            cnt++;
        }
        return sum / cnt;
    }

    public List<T> getMembers() {
        return members;
    }

    public String getFlockName() {
        return flockName;
    }

    public void addDuck(T duck) {
        members.add(duck);
    }

    public void removeDuck(Long id) {
        members.removeIf(duck -> id.equals(duck.getId()));
    }
}
