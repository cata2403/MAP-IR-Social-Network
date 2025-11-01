package com.ubb.business_logic.services;

public class IdProvider {

    private long id;

    Long getId(){
        return id++;
    }

    public void setId(Long id){
        this.id = id;
    }
}
