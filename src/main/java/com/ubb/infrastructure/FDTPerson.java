package com.ubb.infrastructure;

import com.ubb.domain.Entity;
import com.ubb.domain.Person;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FDTPerson implements FileDataTransfer<Long>{
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public String serialization(Entity<Long> o) {
        String fileLine = ((Person) o).getId().toString() + "," + ((Person) o).getUsername() + "," + ((Person) o).getPassword() +  "," + ((Person) o).getEmail() + ',';
        fileLine += ((Person) o).getFirstName() + "," + ((Person) o).getLastName() + "," + ((Person) o).getOccupation() + ',';
        fileLine += ((Person) o).getDateOfBirth().format(formatter);
        return fileLine;
    }

    @Override
    public Entity<Long> deserialization(String s) {
        String[] split = s.split(",");
        Person pers = new Person(Long.parseLong(split[0]),split[1],split[2],split[3]);
        pers = pers.setFirstName(split[4]).setLastName(split[5]).setOccupation(split[6]).setDateOfBirth(LocalDateTime.parse(split[7], formatter));
        return pers;
    }
}
