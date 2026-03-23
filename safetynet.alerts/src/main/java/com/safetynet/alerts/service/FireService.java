package com.safetynet.alerts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.FirePersonDTO;
import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class FireService {

    private final DataLoader dataLoader;
    private final AgeCalculatorService ageCalculatorService;

    public FireService(DataLoader dataLoader, AgeCalculatorService ageCalculatorService) {
        this.dataLoader = dataLoader;
        this.ageCalculatorService = ageCalculatorService;
    }

    public FireResponseDTO getResidentsByAddress(String address) {

        int stationNumber = dataLoader.getData()
                .getFirestations()
                .stream()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address))
                .map(Firestation::getStation)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(0);

        List<FirePersonDTO> residents = dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .map(this::toFirePersonDTO)
                .toList();

        return new FireResponseDTO(stationNumber, residents);
    }

    private FirePersonDTO toFirePersonDTO(Person person) {
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

        return new FirePersonDTO(
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