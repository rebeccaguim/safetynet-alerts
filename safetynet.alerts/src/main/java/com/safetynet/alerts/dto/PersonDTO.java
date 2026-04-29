package com.safetynet.alerts.dto;

/**
 * Data Transfer Object used to represent basic information about a person.
 */
public class PersonDTO {

    // First name of the person
    private String firstName;

    // Last name of the person
    private String lastName;

    // Address of the person
    private String address;

    // Phone number of the person
    private String phone;

    // Constructor to initialize all fields
    public PersonDTO(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
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
     * Gets the phone number
     */
    public String getPhone() {
        return phone;
    }
}