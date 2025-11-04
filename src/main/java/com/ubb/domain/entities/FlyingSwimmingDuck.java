package com.ubb.domain.entities;

import com.ubb.domain.entity_types.DuckType;
import com.ubb.domain.bahaviors.Flyer;
import com.ubb.domain.bahaviors.Swimmer;
import com.ubb.observer.Observer;

public class FlyingSwimmingDuck extends Duck implements Flyer, Swimmer, Observer {

    public FlyingSwimmingDuck(Long id, String username, String password, String email) {
        super(id, username, password, email);
        setDuckType( DuckType.FLYING_AND_SWIMMING );
    }

    @Override
    public void fly() {
        System.out.println("I'm flying! Haha");
    }

    @Override
    public void swim() {
        System.out.println("I'm swimming! Yahoo");
    }

    @Override
    public void update() {
        System.out.println("Stiam ca puteti!");
    }
}
