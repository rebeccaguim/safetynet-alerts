package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class MedicalRecordService {

    private final DataLoader dataLoader;

    public MedicalRecordService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        dataLoader.getData().getMedicalrecords().add(medicalRecord);
        dataLoader.saveData();
        return medicalRecord;
    }

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
        return null;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        boolean removed = dataLoader.getData().getMedicalrecords().removeIf(medicalRecord ->
                medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                        && medicalRecord.getLastName().equalsIgnoreCase(lastName));

        if (removed) {
            dataLoader.saveData();
        }

        return removed;
    }
}