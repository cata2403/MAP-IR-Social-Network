package com.ubb.business_logic.validation;

import com.ubb.domain.Duck;
import com.ubb.domain.Entity;

public class DuckValidationStrategy implements  ValidationStrategy {

    @Override
    public Boolean test(Entity<Long> entity) {

        Duck duck = (Duck) entity;
        if( duck.getUsername().contains(",") )
            return false;

        if( duck.getPassword().contains(",") )
            return false;

        if( duck.getEmail().contains(",") )
            return false;

        if( duck.getSpeed() <= 0 )
            return false;

        if( duck.getResistance() <= 0 )
            return false;

        return true;
    }
}
