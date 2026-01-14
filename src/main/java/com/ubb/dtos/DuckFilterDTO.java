package com.ubb.dtos;

import com.ubb.domain_layer.enums.DuckType;

import java.util.Optional;

public class DuckFilterDTO{

    private Double speed;
    private Double resistance;
    private DuckType type;
    private Long flockId;

    public Optional<Double> getSpeed() {
        return Optional.ofNullable(speed);
    }
    public void setSpeed(Double speed) {
        this.speed = speed;
    }
    public Optional<Double> getResistance() {
        return Optional.ofNullable(resistance);
    }
    public void setResistance(Double resistance) {
        this.resistance = resistance;
    }
    public Optional<DuckType> getType() {
        return Optional.ofNullable(type);
    }
    public void setType(DuckType type) {
        this.type = type;
    }
    public Optional<Long> getFlockId() {
        return Optional.ofNullable(flockId);
    }
    public void setFlockId(Long flockId) {
        this.flockId = flockId;
    }
}

