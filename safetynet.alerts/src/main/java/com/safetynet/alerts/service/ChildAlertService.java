package com.safetynet.alerts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.ChildDTO;
import com.safetynet.alerts.dto.HouseholdMemberDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service is responsible for retrieving children living at a given address.
 */
@Service
public class ChildAlertService {

    // DataLoader is used to access application data
    private final DataLoader dataLoader;

    // Service used to calculate age
    private final AgeCalculatorService ageCalculatorService;

    // Constructor to inject dependencies
    public ChildAlertService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    /**
     * Returns a list of children living at a given address
     *
     * @param address the address to search
     * @return list of children with their age and household members
     */
    public List<ChildDTO> getChildrenByAddress(String address) {

        // Get all persons living at the given address
        List<Person> personsAtAddress = dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        // Filter children and map them to DTO
        return personsAtAddress.stream()
                .filter(this::isChild) // keep only children
                .map(child -> new ChildDTO(
                        child.getFirstName(),
                        child.getLastName(),
                        getAge(child),
                        getHouseholdMembers(child, personsAtAddress)
                ))
                .toList();
    }

    /**
     * Checks if a person is a child
     */
    private boolean isChild(Person person) {

        // Find the medical record of the person
        Optional<MedicalRecord> medicalRecord = findMedicalRecord(person);

        // Check if the age is <= 18
        return medicalRecord
                .map(record -> ageCalculatorService.isChild(record.getBirthdate()))
                .orElse(false);
    }

    /**
     * Returns the age of a person
     */
    private int getAge(Person person) {

        // Find the medical record of the person
        Optional<MedicalRecord> medicalRecord = findMedicalRecord(person);

        // Calculate age if record exists
        return medicalRecord
                .map(record -> ageCalculatorService.calculateAge(record.getBirthdate()))
                .orElse(0);
    }

    /**
     * Finds the medical record of a person
     */
    private Optional<MedicalRecord> findMedicalRecord(Person person) {

        // Search medical record by first name and last name
        return dataLoader.getData()
                .getMedicalrecords()
                .stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();
    }

    /**
     * Returns the list of other household members (excluding the child)
     */
    private List<HouseholdMemberDTO> getHouseholdMembers(Person child, List<Person> personsAtAddress) {

        // Return all persons at the same address except the child
        return personsAtAddress.stream()
                .filter(person -> !(person.getFirstName().equalsIgnoreCase(child.getFirstName())
                        && person.getLastName().equalsIgnoreCase(child.getLastName())))
                .map(person -> new HouseholdMemberDTO(person.getFirstName(), person.getLastName()))
                .toList();
    }
}