package com.ubb.domain.entities;

import com.ubb.domain.entity_types.DuckType;
import com.ubb.domain.bahaviors.Swimmer;
import com.ubb.observer.Observer;

public class SwimmingDuck extends Duck implements Swimmer, Observer {

    public SwimmingDuck(Long id, String username, String password, String email) {
        super(id, username, password, email);
        setDuckType( DuckType.SWIMMING );
    }

    @Override
    public void swim() {
        System.out.println("I'm swimming! Woo");
    }

    @Override
    public void update() {
        System.out.println("Eu puteam obtine scor mai bun!");
    }
}
