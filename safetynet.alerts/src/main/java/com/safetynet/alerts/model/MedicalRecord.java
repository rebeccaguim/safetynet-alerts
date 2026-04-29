package com.safetynet.alerts.model;

import java.util.List;

/**
 * Represents a medical record for a person.
 */
public class MedicalRecord {

    // First name of the person
    private String firstName;

    // Last name of the person
    private String lastName;

    // Birthdate of the person (format: MM/dd/yyyy)
    private String birthdate;

    // List of medications taken by the person
    private List<String> medications;

    // List of allergies of the person
    private List<String> allergies;

    // Default constructor
    public MedicalRecord() {}

    /**
     * Gets the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the birthdate
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the birthdate
     */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Gets the list of medications
     */
    public List<String> getMedications() {
        return medications;
    }

    /**
     * Sets the list of medications
     */
    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    /**
     * Gets the list of allergies
     */
    public List<String> getAllergies() {
        return allergies;
    }

    /**
     * Sets the list of allergies
     */
    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}