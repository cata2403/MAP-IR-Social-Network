package com.ubb.domain;
import java.time.LocalDateTime;

public class Person extends User {
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
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
    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }
    public Person setDateOfBirth(LocalDateTime dateOfBirth) {
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
}
