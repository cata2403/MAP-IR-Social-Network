package com.ubb.business_logic.dtos;

import com.ubb.domain.DuckType;

public class DuckExtrasDTO {
    private final double speed;
    private final double resistance;
    private final DuckType type;
    public DuckExtrasDTO(double speed, double resistance, DuckType type) {
        this.speed = speed;
        this.resistance = resistance;
        this.type = type;
    }
    public double getSpeed() {
        return speed;
    }
    public double getResistance() {
        return resistance;
    }
    public DuckType getType() {
        return type;
    }
}
