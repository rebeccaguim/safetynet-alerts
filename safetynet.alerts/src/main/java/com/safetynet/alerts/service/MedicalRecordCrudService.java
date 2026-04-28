package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.exception.ResourceNotFoundException;
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

        dataLoader.getData().getMedicalrecords().add(medicalRecord);
        dataLoader.saveData();

        return medicalRecord;
    }

    /**
     * Updates an existing medical record using first name and last name
     *
     * @param updatedMedicalRecord the updated medical record
     * @return the updated medical record
     */
    public MedicalRecord updateMedicalRecord(MedicalRecord updatedMedicalRecord) {

        for (MedicalRecord medicalRecord : dataLoader.getData().getMedicalrecords()) {

            if (medicalRecord.getFirstName().equalsIgnoreCase(updatedMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equalsIgnoreCase(updatedMedicalRecord.getLastName())) {

                medicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
                medicalRecord.setMedications(updatedMedicalRecord.getMedications());
                medicalRecord.setAllergies(updatedMedicalRecord.getAllergies());

                dataLoader.saveData();

                return medicalRecord;
            }
        }

        //  Replace null with exception
        throw new ResourceNotFoundException(
                "Medical record not found: "
                        + updatedMedicalRecord.getFirstName() + " "
                        + updatedMedicalRecord.getLastName()
        );
    }

    /**
     * Deletes a medical record using first name and last name
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return true if deleted
     */
    public boolean deleteMedicalRecord(String firstName, String lastName) {

        boolean removed = dataLoader.getData().getMedicalrecords().removeIf(medicalRecord ->
                medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                        && medicalRecord.getLastName().equalsIgnoreCase(lastName));

        if (removed) {
            dataLoader.saveData();
            return true;
        }

        // Replace false with exception
        throw new ResourceNotFoundException(
                "Medical record not found: " + firstName + " " + lastName
        );
    }
}