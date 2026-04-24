package com.safetynet.alerts.dto;

/**
 * Data Transfer Object used to represent a household member.
 */
public class HouseholdMemberDTO {

    // First name of the household member
    private String firstName;

    // Last name of the household member
    private String lastName;

    // Constructor to initialize fields
    public HouseholdMemberDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
}