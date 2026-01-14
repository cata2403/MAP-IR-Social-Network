package com.ubb.domain_layer.validation;

import com.ubb.domain_layer.entities.Entity;
import com.ubb.exceptions.ValidationException;

public class Validator {

    private ValidationStrategy strategy;

    public Validator(ValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public Validator(){}

    public void setStrategy(ValidationStrategy strategy){
        this.strategy = strategy;
    }

    public void validate(Entity<Long> entity) throws ValidationException {
        if( !strategy.test(entity) )
            throw new ValidationException("<<Validation Failed>>");
    }
}

