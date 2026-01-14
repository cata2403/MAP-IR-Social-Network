package com.ubb.domain_layer.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Flock<T extends Duck> extends Entity<Long>{

    private final String flockName;
    private final List<T> members = new ArrayList<>();

    /**
     * Creates an instance of a flock, an entity representing a group of ducks
     * @param id flock id
     * @param flockName flock's name
     */
    public Flock(Long id, String flockName) {
        super(id);
        this.flockName = flockName;
    }

    /**
     * Returns all the ducks from the flock
     * @return the ducks (members) of the flock
     */
    public List<T> getMembers() {
        return members;
    }

    /**
     * Returns the name of the flock
     * @return flock's name
     */
    public String getFlockName() {
        return flockName;
    }

    /**
     * Adds a new duck in the flock
     * @param duck the duck that will be added into the flock
     */
    public void addDuck(T duck) {

        duck.setFlockId(this.getId());
        members.add(duck);
    }

    /**
     * Removes a duck from the flock
     * @param outDuck the duck that needs to leave the flock
     */
    public void removeDuck(T outDuck) {
        members.removeIf(duck -> duck.equals(outDuck));
        outDuck.setFlockId(-1L);
    }
}
