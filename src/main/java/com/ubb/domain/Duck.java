package com.ubb.domain;

public class Duck extends User{
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
}
