package com.safetynet.alerts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service is responsible for retrieving detailed information about persons.
 */
@Service
public class PersonInfoService {

    // DataLoader is used to access application data
    private final DataLoader dataLoader;

    // Service used to calculate age
    private final AgeCalculatorService ageCalculatorService;

    // Constructor to inject dependencies
    public PersonInfoService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    /**
     * Returns detailed information about persons with a given last name
     *
     * @param lastName the last name to search
     * @return list of PersonInfoDTO
     */
    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {

        // Get all persons with the given last name
        return dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .map(this::toPersonInfoDTO) // convert to DTO
                .toList();
    }

    /**
     * Converts a Person into PersonInfoDTO with medical information
     */
    private PersonInfoDTO toPersonInfoDTO(Person person) {

        // Find medical record for the person
        Optional<MedicalRecord> medicalRecord = findMedicalRecord(person);

        // Calculate age
        int age = medicalRecord
                .map(record -> ageCalculatorService.calculateAge(record.getBirthdate()))
                .orElse(0);

        // Get medications
        List<String> medications = medicalRecord
                .map(MedicalRecord::getMedications)
                .orElse(List.of());

        // Get allergies
        List<String> allergies = medicalRecord
                .map(MedicalRecord::getAllergies)
                .orElse(List.of());

        // Create and return DTO
        return new PersonInfoDTO(
                person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                age,
                person.getEmail(),
                medications,
                allergies
        );
    }

    /**
     * Finds the medical record of a person using first name and last name
     */
    private Optional<MedicalRecord> findMedicalRecord(Person person) {

        // Search medical record in data
        return dataLoader.getData()
                .getMedicalrecords()
                .stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();
    }
}