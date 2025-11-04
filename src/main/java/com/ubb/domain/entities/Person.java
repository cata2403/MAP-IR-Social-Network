package com.ubb.domain.entities;
import com.ubb.observer.Observer;

import java.io.Serializable;
import java.time.LocalDate;

public class Person extends User implements Observer{

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String occupation;

    public Person(Long id, String username, String password, String email) {
        super(id, username, password, email);
    }

    public String getFirstName() {
        return firstName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Person setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getOccupation() {
        return occupation;
    }

    public Person setOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    @Override
    public void update() {
        System.out.println(firstName + lastName + ": Eu ce caut aici?");
    }
}
