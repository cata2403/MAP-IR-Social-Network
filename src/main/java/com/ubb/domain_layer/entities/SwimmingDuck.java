package com.ubb.domain_layer.entities;

import com.ubb.domain_layer.behaviors.Swimmer;
import com.ubb.domain_layer.enums.DuckType;

public class SwimmingDuck extends Duck implements Swimmer {

    /**
     * Creates an instance of a swimming duck
     * @param id duck's id
     * @param username duck's username
     * @param password duck's password
     * @param email duck's email
     */
    public SwimmingDuck(Long id, String username, String password, String email) {
        super(id, username, password, email);
        setDuckType( DuckType.SWIMMING );
    }

    @Override
    public void swim() {
    }
}

