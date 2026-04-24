package com.safetynet.alerts.dto;

import java.util.List;

/**
 * Data Transfer Object used to represent a person in a flood situation.
 */
public class FloodPersonDTO {

    // First name of the person
    private String firstName;

    // Last name of the person
    private String lastName;

    // Phone number of the person
    private String phone;

    // Age of the person
    private int age;

    // List of medications taken by the person
    private List<String> medications;

    // List of allergies of the person
    private List<String> allergies;

    // Constructor to initialize all fields
    public FloodPersonDTO(String firstName, String lastName, String phone, int age,
                          List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
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
     * Gets the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the age
     */
    public int getAge() {
        return age;
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