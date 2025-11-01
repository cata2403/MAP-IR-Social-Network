package com.ubb.domain;

import com.ubb.business_logic.dtos.DuckExtrasDTO;
import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.business_logic.dtos.PersonExtrasDTO;
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

        Duck duck = new Duck(id, dto1.username(), dto1.password(), dto1.email());
        duck = duck.setSpeed(dto2.speed()).
                    setResistance(dto2.resistance()).
                    setDuckType(dto2.type());
        return duck;
    }
}
