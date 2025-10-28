package com.ubb.domain;
import java.util.ArrayList;
import java.util.List;

public class Flock extends Entity<Long>{
    private String flockName;
    private final List<Duck> members = new ArrayList<Duck>();
    public Flock(Long id) {
        super(id);
    }
    public Double getAveragePerformance() {
        Double sum = 0.0, cnt = 0.0;
        for (Duck duck : members) {
            sum += duck.getSpeed();
            cnt++;
        }
        return sum/cnt;
    }
    public String getFlockName() {
        return flockName;
    }
    public void addDuck(Duck duck) {
        members.add(duck);
    }
    public void removeDuck(Duck duck) {
        members.remove(duck);
    }
}
