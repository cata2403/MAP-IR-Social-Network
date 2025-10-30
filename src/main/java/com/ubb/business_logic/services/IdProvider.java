package com.ubb.business_logic.services;

public class IdProvider {
    private static long id;
    static Long getId(){return id++;}
    public static void setId(Long id){
        IdProvider.id = id;
    }
}
