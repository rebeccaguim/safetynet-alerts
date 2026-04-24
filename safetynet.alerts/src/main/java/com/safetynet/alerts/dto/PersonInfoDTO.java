package com.safetynet.alerts.dto;

import java.util.List;

/**
 * Data Transfer Object used to represent detailed information about a person.
 */
public class PersonInfoDTO {

    // First name of the person
    private String firstName;

    // Last name of the person
    private String lastName;

    // Address of the person
    private String address;

    // Age of the person
    private int age;

    // Email address of the person
    private String email;

    // List of medications taken by the person
    private List<String> medications;

    // List of allergies of the person
    private List<String> allergies;

    // Constructor to initialize all fields
    public PersonInfoDTO(String firstName, String lastName, String address, int age,
                         String email, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.age = age;
        this.email = email;
        this.medications = medications;
        this.allergies = allergies;
    }

    /**
     * Gets the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the medications
     */
    public List<String> getMedications() {
        return medications;
    }

    /**
     * Gets the allergies
     */
    public List<String> getAllergies() {
        return allergies;
    }
}