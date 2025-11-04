package com.ubb.domain.entities;

import com.ubb.domain.entity_types.DuckType;
import com.ubb.domain.bahaviors.Flyer;
import com.ubb.observer.Observer;

public class FlyingDuck extends Duck implements Flyer, Observer {
    public FlyingDuck(Long id, String username, String password, String email) {
        super(id, username, password, email);
        setDuckType( DuckType.FLYING );
    }

    @Override
    public void fly() {
        System.out.println("I'm flying! Wee");
    }

    @Override
    public void update() {
        System.out.println("Binee maaa! Bravoo");
    }
}
