package com.safetynet.alerts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.ChildDTO;
import com.safetynet.alerts.dto.HouseholdMemberDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class ChildAlertService {

    private final DataLoader dataLoader;
    private final AgeCalculatorService ageCalculatorService;

    public ChildAlertService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    public List<ChildDTO> getChildrenByAddress(String address) {

        List<Person> personsAtAddress = dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        return personsAtAddress.stream()
                .filter(this::isChild)
                .map(child -> new ChildDTO(
                        child.getFirstName(),
                        child.getLastName(),
                        getAge(child),
                        getHouseholdMembers(child, personsAtAddress)
                ))
                .toList();
    }

    private boolean isChild(Person person) {
        Optional<MedicalRecord> medicalRecord = findMedicalRecord(person);
        return medicalRecord
                .map(record -> ageCalculatorService.isChild(record.getBirthdate()))
                .orElse(false);
    }

    private int getAge(Person person) {
        Optional<MedicalRecord> medicalRecord = findMedicalRecord(person);
        return medicalRecord
                .map(record -> ageCalculatorService.calculateAge(record.getBirthdate()))
                .orElse(0);
    }

    private Optional<MedicalRecord> findMedicalRecord(Person person) {
        return dataLoader.getData()
                .getMedicalrecords()
                .stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();
    }

    private List<HouseholdMemberDTO> getHouseholdMembers(Person child, List<Person> personsAtAddress) {
        return personsAtAddress.stream()
                .filter(person -> !(person.getFirstName().equalsIgnoreCase(child.getFirstName())
                        && person.getLastName().equalsIgnoreCase(child.getLastName())))
                .map(person -> new HouseholdMemberDTO(person.getFirstName(), person.getLastName()))
                .toList();
    }
}