package com.ubb.domain_layer.entities;


import com.ubb.domain_layer.behaviors.Flyer;
import com.ubb.domain_layer.enums.DuckType;

public class FlyingDuck extends Duck implements Flyer{

    /**
     * Creates an instance of a flying duck
     * @param id duck's id
     * @param username duck's username
     * @param password duck's password
     * @param email duck's email
     */
    public FlyingDuck(Long id, String username, String password, String email) {
        super(id, username, password, email);
        setDuckType( DuckType.FLYING );
    }

    @Override
    public void fly() {
    }

}

