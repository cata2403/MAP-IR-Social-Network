package com.ubb.domain_layer.entities;
import com.ubb.domain_layer.behaviors.Flyer;
import com.ubb.domain_layer.behaviors.Swimmer;
import com.ubb.domain_layer.enums.DuckType;

public class FlyingSwimmingDuck extends Duck implements Flyer, Swimmer {

    /**
     * Creates an instance of a flying and swimming duck
     * @param id duck's id
     * @param username duck's username
     * @param password duck's password
     * @param email duck's email
     */
    public FlyingSwimmingDuck(Long id, String username, String password, String email) {
        super(id, username, password, email);
        setDuckType( DuckType.FLYING_AND_SWIMMING );
    }

    @Override
    public void fly() {
    }

    @Override
    public void swim() {
    }
}

