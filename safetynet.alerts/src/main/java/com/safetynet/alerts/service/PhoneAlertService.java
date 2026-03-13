package com.safetynet.alerts.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class PhoneAlertService {

    private final DataLoader dataLoader;

    public PhoneAlertService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public Set<String> getPhoneNumbersByStation(int stationNumber) {

        List<String> addresses = dataLoader.getData()
                .getFirestations()
                .stream()
                .filter(fs -> Integer.parseInt(fs.getStation()) == stationNumber)
                .map(Firestation::getAddress)
                .toList();

        return dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .collect(Collectors.toSet());
    }
}