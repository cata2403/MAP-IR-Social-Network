package com.ubb.business_logic.dtos;

public class PersonExtrasDTO {
    private final String firstName;
    private final String lastName;
    private final String occupation;
    private final String birthDate;
    public PersonExtrasDTO(String firstName, String lastName, String occupation, String birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.birthDate = birthDate;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getOccupation() {
        return occupation;
    }
    public String getBirthDate() {
        return birthDate;
    }
}
