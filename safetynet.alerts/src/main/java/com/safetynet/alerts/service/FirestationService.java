package com.safetynet.alerts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.FirestationResponseDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class FirestationService {

    private final DataLoader dataLoader;

    public FirestationService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
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
                .filter(p -> addresses.contains(p.getAddress()))
                .toList();

        List<PersonDTO> personDTOList = persons.stream()
                .map(p -> new PersonDTO(
                        p.getFirstName(),
                        p.getLastName(),
                        p.getAddress(),
                        p.getPhone()))
                .collect(Collectors.toList());

        int adults = persons.size(); // simplifié pour l'instant
        int children = 0;

        return new FirestationResponseDTO(personDTOList, adults, children);
    }
}