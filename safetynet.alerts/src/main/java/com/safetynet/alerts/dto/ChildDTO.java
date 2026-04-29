package com.safetynet.alerts.dto;

import java.util.List;

/**
 * Data Transfer Object used to represent a child with their household members.
 */
public class ChildDTO {

    // First name of the child
    private String firstName;

    // Last name of the child
    private String lastName;

    // Age of the child
    private int age;

    // List of other household members living at the same address
    private List<HouseholdMemberDTO> householdMembers;

    // Constructor to initialize all fields
    public ChildDTO(String firstName, String lastName, int age, List<HouseholdMemberDTO> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
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
     * Gets the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the household members
     */
    public List<HouseholdMemberDTO> getHouseholdMembers() {
        return householdMembers;
    }
}