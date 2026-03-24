package com.safetynet.alerts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class PersonInfoService {

    private final DataLoader dataLoader;
    private final AgeCalculatorService ageCalculatorService;

    public PersonInfoService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {
        return dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .map(this::toPersonInfoDTO)
                .toList();
    }

    private PersonInfoDTO toPersonInfoDTO(Person person) {
        Optional<MedicalRecord> medicalRecord = findMedicalRecord(person);

        int age = medicalRecord
                .map(record -> ageCalculatorService.calculateAge(record.getBirthdate()))
                .orElse(0);

        List<String> medications = medicalRecord
                .map(MedicalRecord::getMedications)
                .orElse(List.of());

        List<String> allergies = medicalRecord
                .map(MedicalRecord::getAllergies)
                .orElse(List.of());

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

    private Optional<MedicalRecord> findMedicalRecord(Person person) {
        return dataLoader.getData()
                .getMedicalrecords()
                .stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();
    }
}