package com.safetynet.alerts.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.FirePersonDTO;
import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service is responsible for retrieving residents information for a given address.
 */
@Service
public class FireService {

    // Logger used to record service activity
    private static final Logger logger = LoggerFactory.getLogger(FireService.class);

    // DataLoader is used to access application data
    private final DataLoader dataLoader;

    // Service used to calculate age
    private final AgeCalculatorService ageCalculatorService;

    // Constructor to inject dependencies
    public FireService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    /**
     * Returns residents living at a given address with their firestation number
     *
     * @param address the address to search
     * @return FireResponseDTO containing station number and residents list
     */
    public FireResponseDTO getResidentsByAddress(String address) {

        // Log the start of the request
        logger.info("Fetching residents for address: {}", address);

        // Find the firestation number for the given address
        int stationNumber = dataLoader.getData()
                .getFirestations()
                .stream()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address))
                .map(Firestation::getStation)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(0);

        // Get all persons living at the given address
        List<FirePersonDTO> residents = dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .map(this::toFirePersonDTO) // convert to DTO
                .toList();

        // Log the result
        logger.info("Found {} resident(s) for address {} covered by station {}", residents.size(), address, stationNumber);

        // Return result as DTO
        return new FireResponseDTO(stationNumber, residents);
    }

    /**
     * Converts a Person into FirePersonDTO with medical information
     */
    private FirePersonDTO toFirePersonDTO(Person person) {

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
        return new FirePersonDTO(
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