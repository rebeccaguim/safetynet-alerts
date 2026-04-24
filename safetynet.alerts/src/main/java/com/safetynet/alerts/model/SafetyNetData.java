package com.safetynet.alerts.model;

import java.util.List;

/**
 * Represents all application data loaded from the JSON file.
 */
public class SafetyNetData {

    // List of all persons
    private List<Person> persons;

    // List of all firestation mappings
    private List<Firestation> firestations;

    // List of all medical records
    private List<MedicalRecord> medicalrecords;

    // Default constructor
    public SafetyNetData() {}

    /**
     * Gets the list of persons
     */
    public List<Person> getPersons() {
        return persons;
    }

    /**
     * Sets the list of persons
     */
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    /**
     * Gets the list of firestations
     */
    public List<Firestation> getFirestations() {
        return firestations;
    }

    /**
     * Sets the list of firestations
     */
    public void setFirestations(List<Firestation> firestations) {
        this.firestations = firestations;
    }

    /**
     * Gets the list of medical records
     */
    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }

    /**
     * Sets the list of medical records
     */
    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }
}