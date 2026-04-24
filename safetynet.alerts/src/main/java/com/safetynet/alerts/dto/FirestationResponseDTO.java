package com.safetynet.alerts.dto;

import java.util.List;

/**
 * Data Transfer Object used to represent the response for a firestation.
 */
public class FirestationResponseDTO {

    // List of persons covered by the firestation
    private List<PersonDTO> persons;

    // Number of adults
    private int numberOfAdults;

    // Number of children
    private int numberOfChildren;

    // Constructor to initialize all fields
    public FirestationResponseDTO(List<PersonDTO> persons, int numberOfAdults, int numberOfChildren) {
        this.persons = persons;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    /**
     * Gets the list of persons
     */
    public List<PersonDTO> getPersons() {
        return persons;
    }

    /**
     * Gets the number of adults
     */
    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    /**
     * Gets the number of children
     */
    public int getNumberOfChildren() {
        return numberOfChildren;
    }
}