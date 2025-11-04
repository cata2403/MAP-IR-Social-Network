package com.ubb.domain.entities;

import com.ubb.domain.entity_types.DuckType;

import java.io.Serializable;
import java.util.Objects;

public abstract class Duck extends User implements Comparable<Duck>{

    private DuckType duckType;
    private Double speed;
    private Double resistance;
    private Long flockId = -1L;

    public Duck(Long id, String username, String password, String email) {
        super(id, username, password, email);
    }

    public DuckType getDuckType() {
        return duckType;
    }

    public Duck setDuckType(DuckType duckType) {
        this.duckType = duckType;
        return this;
    }

    public Double getSpeed() {
        return speed;
    }

    public Duck setSpeed(Double speed) {
        this.speed = speed;
        return this;
    }

    public Double getResistance() {
        return resistance;
    }

    public Duck setResistance(Double resistance) {
        this.resistance = resistance;
        return this;
    }

    public Long getFlockId() {
        return flockId;
    }

    public Duck setFlockId(Long flockId) {
        this.flockId = flockId;
        return this;
    }

    public int compareTo(Duck duck) {
        if(Objects.equals(resistance, duck.getResistance()))
            return speed.compareTo(duck.getSpeed());
        return resistance.compareTo(duck.getResistance());
    }
}
