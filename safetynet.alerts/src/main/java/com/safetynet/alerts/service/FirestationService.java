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

@Service
public class FirestationService {

    private final DataLoader dataLoader;
    private final AgeCalculatorService ageCalculatorService;

    public FirestationService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    public FirestationResponseDTO getPersonsByStation(int stationNumber) {

        List<String> addresses = dataLoader.getData()
                .getFirestations()
                .stream()
                .filter(fs -> Integer.parseInt(fs.getStation()) == stationNumber)
                .map(Firestation::getAddress)
                .toList();

        List<Person> persons = dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();

        List<PersonDTO> personDTOList = persons.stream()
                .map(person -> new PersonDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()
                ))
                .collect(Collectors.toList());

        int numberOfChildren = (int) persons.stream()
                .filter(this::isChild)
                .count();

        int numberOfAdults = persons.size() - numberOfChildren;

        return new FirestationResponseDTO(personDTOList, numberOfAdults, numberOfChildren);
    }

    private boolean isChild(Person person) {
        Optional<MedicalRecord> medicalRecord = dataLoader.getData()
                .getMedicalrecords()
                .stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();

        return medicalRecord
                .map(record -> ageCalculatorService.isChild(record.getBirthdate()))
                .orElse(false);
    }
}