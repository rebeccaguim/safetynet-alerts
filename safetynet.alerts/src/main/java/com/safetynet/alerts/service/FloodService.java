package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.dto.FloodPersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service is responsible for retrieving households covered by multiple firestations.
 */
@Service
public class FloodService {

    // DataLoader is used to access application data
    private final DataLoader dataLoader;

    // Service used to calculate age
    private final AgeCalculatorService ageCalculatorService;

    // Constructor to inject dependencies
    public FloodService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    /**
     * Returns households grouped by address for a list of firestations
     *
     * @param stations list of firestation numbers
     * @return list of FloodAddressDTO containing residents by address
     */
    public List<FloodAddressDTO> getFloodStations(List<Integer> stations) {

        // Get all addresses covered by the given stations
        Set<String> addresses = dataLoader.getData()
                .getFirestations()
                .stream()
                .filter(firestation -> stations.contains(Integer.parseInt(firestation.getStation())))
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());

        // Group persons by address
        Map<String, List<Person>> personsByAddress = dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.groupingBy(Person::getAddress));

        // Convert each group into a DTO
        return personsByAddress.entrySet()
                .stream()
                .map(entry -> new FloodAddressDTO(
                        entry.getKey(), // address
                        entry.getValue().stream()
                                .map(this::toFloodPersonDTO) // convert each person
                                .toList()
                ))
                .toList();
    }

    /**
     * Converts a Person into FloodPersonDTO with medical information
     */
    private FloodPersonDTO toFloodPersonDTO(Person person) {

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
        return new FloodPersonDTO(
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                age,
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