package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service handles CRUD operations for medical records.
 */
@Service
public class MedicalRecordCrudService {

    // DataLoader is used to access and save application data
    private final DataLoader dataLoader;

    // Constructor to inject the DataLoader
    public MedicalRecordCrudService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Adds a new medical record
     *
     * @param medicalRecord the medical record to add
     * @return the added medical record
     */
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {

        // Add medical record to the list
        dataLoader.getData().getMedicalrecords().add(medicalRecord);

        // Save updated data to JSON file
        dataLoader.saveData();

        // Return the added medical record
        return medicalRecord;
    }

    /**
     * Updates an existing medical record using first name and last name
     *
     * @param updatedMedicalRecord the updated medical record
     * @return the updated medical record or null if not found
     */
    public MedicalRecord updateMedicalRecord(MedicalRecord updatedMedicalRecord) {

        // Loop through all medical records
        for (MedicalRecord medicalRecord : dataLoader.getData().getMedicalrecords()) {

            // Check if first name and last name match
            if (medicalRecord.getFirstName().equalsIgnoreCase(updatedMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equalsIgnoreCase(updatedMedicalRecord.getLastName())) {

                // Update fields
                medicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
                medicalRecord.setMedications(updatedMedicalRecord.getMedications());
                medicalRecord.setAllergies(updatedMedicalRecord.getAllergies());

                // Save updated data
                dataLoader.saveData();

                // Return updated record
                return medicalRecord;
            }
        }

        // Return null if no record is found
        return null;
    }

    /**
     * Deletes a medical record using first name and last name
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return true if deleted, false otherwise
     */
    public boolean deleteMedicalRecord(String firstName, String lastName) {

        // Remove medical record if names match
        boolean removed = dataLoader.getData().getMedicalrecords().removeIf(medicalRecord ->
                medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                        && medicalRecord.getLastName().equalsIgnoreCase(lastName));

        // Save data only if something was removed
        if (removed) {
            dataLoader.saveData();
        }

        return removed;
    }
}