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

@Service
public class FloodService {

    private final DataLoader dataLoader;
    private final AgeCalculatorService ageCalculatorService;

    public FloodService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    public List<FloodAddressDTO> getFloodStations(List<Integer> stations) {

        Set<String> addresses = dataLoader.getData()
                .getFirestations()
                .stream()
                .filter(firestation -> stations.contains(Integer.parseInt(firestation.getStation())))
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());

        Map<String, List<Person>> personsByAddress = dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.groupingBy(Person::getAddress));

        return personsByAddress.entrySet()
                .stream()
                .map(entry -> new FloodAddressDTO(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(this::toFloodPersonDTO)
                                .toList()
                ))
                .toList();
    }

    private FloodPersonDTO toFloodPersonDTO(Person person) {
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

        return new FloodPersonDTO(
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                age,
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