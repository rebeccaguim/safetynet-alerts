package com.safetynet.alerts.dto;

import java.util.List;

public class ChildDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<HouseholdMemberDTO> householdMembers;

    public ChildDTO(String firstName, String lastName, int age, List<HouseholdMemberDTO> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public List<HouseholdMemberDTO> getHouseholdMembers() {
        return householdMembers;
    }
}