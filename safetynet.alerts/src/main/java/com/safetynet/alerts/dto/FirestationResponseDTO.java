package com.safetynet.alerts.dto;

import java.util.List;

public class FirestationResponseDTO {

    private List<PersonDTO> persons;
    private int numberOfAdults;
    private int numberOfChildren;

    public FirestationResponseDTO(List<PersonDTO> persons, int numberOfAdults, int numberOfChildren) {
        this.persons = persons;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }
}