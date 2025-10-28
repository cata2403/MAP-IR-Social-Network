package com.ubb.domain;
import java.util.ArrayList;
import java.util.List;

public class Flock extends Entity<Long>{
    private final String flockName;
    private final List<Duck> members = new ArrayList<Duck>();
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
        return sum/cnt;
    }
    public String getFlockName() {
        return flockName;
    }
    public void addDuck(Duck duck) {
        members.add(duck);
    }
    public void removeDuck(Long id) {
        members.removeIf(duck -> duck.getId().equals(id));
    }
}
