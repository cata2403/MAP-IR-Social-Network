package com.ubb.domain_layer.entities;

import java.time.LocalDate;

public class Person extends User {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String occupation;

    /**
     * Creates an instance of a person user
     * @param id the person's id
     * @param username the person's username
     * @param password the person's password
     * @param email the person's email
     */
    public Person(Long id, String username, String password, String email) {
        super(id, username, password, email);
    }

    /**
     * Returns the first name of the person
     * @return person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Changes the first name of a person
     * @param firstName new person's first name
     * @return the instance of the person after the first name change
     */
    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Returns the last name of the person
     * @return person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Changes the last name of the person
     * @param lastName new person's last name
     * @return the instance of the person with updated last name
     */
    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Returns the birthdate of the person
     * @return person's birthdate
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Changes the birthdate of the person
     * @param dateOfBirth the new birthdate
     * @return the intance of the person with updated birthdate
     */
    public Person setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    /**
     * Returns the occupation of the person
     * @return person's occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Changes the occupation of the person
     * @param occupation the new occupation
     * @return the instance of the person with updated occupation
     */
    public Person setOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }
}

