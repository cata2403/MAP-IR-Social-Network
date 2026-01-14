package com.ubb.application_layer.factories;

import com.ubb.domain_layer.entities.*;
import com.ubb.domain_layer.enums.DuckType;
import com.ubb.dtos.DuckExtrasDTO;
import com.ubb.dtos.FullUserInfoDTO;
import com.ubb.dtos.PersonExtrasDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserFactory {

    public static User createUser(FullUserInfoDTO dto1, PersonExtrasDTO dto2, Long id) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Person person = new Person(id, dto1.username(), dto1.password(), dto1.email());
        person = person.setFirstName(dto2.firstName()).
                setLastName(dto2.lastName()).
                setOccupation(dto2.occupation()).
                setDateOfBirth(LocalDate.parse(dto2.birthDate(), formatter));
        return person;
    }

    public static User createUser(FullUserInfoDTO dto1, DuckExtrasDTO dto2, Long id) {

        Duck duck;
        if( dto2.type() == DuckType.FLYING ){
            duck = new FlyingDuck(id, dto1.username(), dto1.password(), dto1.email());
        }

        else if( dto2.type() == DuckType.SWIMMING ){
            duck = new SwimmingDuck(id, dto1.username(), dto1.password(), dto1.email());
        }

        else duck = new FlyingSwimmingDuck(id, dto1.username(), dto1.password(), dto1.email());

        duck = duck.setSpeed(dto2.speed()).
                setResistance(dto2.resistance()).
                setDuckType(dto2.type());
        return duck;
    }
}

