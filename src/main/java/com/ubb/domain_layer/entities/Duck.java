package com.ubb.domain_layer.entities;

import com.ubb.domain_layer.enums.DuckType;

import java.util.Objects;

public abstract class Duck extends User implements Comparable<Duck>{

    private DuckType duckType;
    private Double speed;
    private Double resistance;
    private Long flockId = -1L;

    /**
     * Creates an instance of a Duck which is a user
     * @param id the duck's id
     * @param username the duck's username
     * @param password the duck's password
     * @param email the duck's email
     */
    public Duck(Long id, String username, String password, String email) {
        super(id, username, password, email);
    }

    /**
     * Returns the type of the duck
     * @return duck's type
     */
    public DuckType getDuckType() {
        return duckType;
    }

    /**
     * Changes the type of the duck
     * @param duckType the new duck type
     * @return the duck instance with updated duck type
     */
    public Duck setDuckType(DuckType duckType) {
        this.duckType = duckType;
        return this;
    }

    /**
     * Returns the speed of the duck
     * @return duck's speed
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * Chanfes the speed of the duck
     * @param speed new duck speed
     * @return the duck instance with updated speed
     */
    public Duck setSpeed(Double speed) {
        this.speed = speed;
        return this;
    }

    /**
     * Returns the resistance of the duck
     * @return duck's resistance
     */
    public Double getResistance() {
        return resistance;
    }

    /**
     * Changes the resistance of the duck
     * @param resistance duck's resistance
     * @return the duck instance with updates resistance
     */
    public Duck setResistance(Double resistance) {
        this.resistance = resistance;
        return this;
    }

    /**
     * Returns the id of the flock the duck is part of
     * @return the flock id, -1 if the duck doesn't belong to a flock
     */
    public Long getFlockId() {
        return flockId;
    }

    /**
     * Changes the id of the flock the duck belongs to
     * @param flockId the new flock id
     * @return the duck instance with updated flock id
     */
    public Duck setFlockId(Long flockId) {
        this.flockId = flockId;
        return this;
    }

    /**
     * Compares two ducks for the purpose of sorting by speed and resistance
     * @param duck the duck the instance is compared to
     * @return
     * -1 if the instance should be put first
     * 0 if the ducks are equal by speed/resistance
     * 1 if the instance should be put second
     */
    public int compareTo(Duck duck) {
        if(Objects.equals(resistance, duck.getResistance()))
            return speed.compareTo(duck.getSpeed());
        return resistance.compareTo(duck.getResistance());
    }
}