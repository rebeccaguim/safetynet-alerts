package com.safetynet.alerts.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.FirestationResponseDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service is responsible for retrieving persons covered by a firestation.
 */
@Service
public class FirestationService {

    // DataLoader is used to access application data
    private final DataLoader dataLoader;

    // Service used to calculate age
    private final AgeCalculatorService ageCalculatorService;

    // Constructor to inject dependencies
    public FirestationService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    /**
     * Returns all persons covered by a firestation with number of adults and children
     *
     * @param stationNumber the firestation number
     * @return FirestationResponseDTO containing persons and counts
     */
    public FirestationResponseDTO getPersonsByStation(int stationNumber) {

        // Get all addresses covered by the given firestation
        List<String> addresses = dataLoader.getData()
                .getFirestations()
                .stream()
                .filter(fs -> Integer.parseInt(fs.getStation()) == stationNumber)
                .map(Firestation::getAddress)
                .toList();

        // Get all persons living at these addresses
        List<Person> persons = dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();

        // Convert persons into PersonDTO
        List<PersonDTO> personDTOList = persons.stream()
                .map(person -> new PersonDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()
                ))
                .collect(Collectors.toList());

        // Count number of children
        int numberOfChildren = (int) persons.stream()
                .filter(this::isChild)
                .count();

        // Calculate number of adults
        int numberOfAdults = persons.size() - numberOfChildren;

        // Return the result as DTO
        return new FirestationResponseDTO(personDTOList, numberOfAdults, numberOfChildren);
    }

    /**
     * Checks if a person is a child
     */
    private boolean isChild(Person person) {

        // Find medical record for the person
        Optional<MedicalRecord> medicalRecord = dataLoader.getData()
                .getMedicalrecords()
                .stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();

        // Check if age is <= 18
        return medicalRecord
                .map(record -> ageCalculatorService.isChild(record.getBirthdate()))
                .orElse(false);
    }
}