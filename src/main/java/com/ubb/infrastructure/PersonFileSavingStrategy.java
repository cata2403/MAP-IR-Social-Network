package com.ubb.infrastructure;

import com.ubb.domain.entities.Person;
import com.ubb.utils.DateTimeFormats;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PersonFileSavingStrategy implements DataTransferStrategy<Long, Person> {

    @Override
    public String serialize(Person person) {

        return String.join(",",
                String.valueOf(person.getId()),
                person.getUsername(),
                person.getPassword(),
                person.getEmail(),
                person.getFirstName(),
                person.getLastName(),
                person.getOccupation(),
                person.getDateOfBirth()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    @Override
    public Person deserialize(String fileLine) {

        String[] personData = fileLine.split(",");

        Person person = new Person( Long.parseLong(personData[0]), personData[1], personData[2], personData[3] );
        person = person.setFirstName(personData[4]).
                        setLastName(personData[5]).
                        setOccupation(personData[6]).
                        setDateOfBirth( LocalDate.parse(personData[7], DateTimeFormats.getDateFormatter()) );

        return person;
    }
}
