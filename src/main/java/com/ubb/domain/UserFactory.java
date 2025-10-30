package com.ubb.domain;

import com.ubb.business_logic.dtos.DuckExtrasDTO;
import com.ubb.business_logic.dtos.FullUserInfoDTO;
import com.ubb.business_logic.dtos.PersonExtrasDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserFactory {
    public static User createUser(FullUserInfoDTO dto1, PersonExtrasDTO dto2, Long id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Person person = new Person(id,dto1.getUsername(),dto1.getPassword(),dto1.getEmail());
        person = person.setFirstName(dto2.getFirstName()).setLastName(dto2.getLastName()).
                setOccupation(dto2.getOccupation()).setDateOfBirth(LocalDate.parse(dto2.getBirthDate(), formatter));
        return person;
    }
    public static User createUser(FullUserInfoDTO dto1, DuckExtrasDTO dto2, Long id) {
        Duck duck = new Duck(id,dto1.getUsername(),dto1.getPassword(),dto1.getEmail());
        duck = duck.setSpeed(dto2.getSpeed()).setResistance(dto2.getResistance()).setDuckType(dto2.getType());
        return duck;
    }
}
