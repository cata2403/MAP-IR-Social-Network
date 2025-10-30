package com.ubb.business_logic.validation;

import com.ubb.domain.Entity;
import com.ubb.domain.Person;

public class PersonValidationStrategy implements ValidationStrategy{
    @Override
    public Boolean test(Entity<Long> entity) {
        Person pers = (Person) entity;
        if(pers.getUsername().contains(","))
            return false;
        if(pers.getPassword().contains(","))
            return false;
        if(pers.getEmail().contains(","))
            return false;
        if(pers.getLastName().contains(","))
            return false;
        if(pers.getFirstName().contains(","))
            return false;
        if(pers.getOccupation().equals(","))
            return false;
        return true;
    }
}
