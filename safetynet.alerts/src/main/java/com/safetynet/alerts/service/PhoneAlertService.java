package com.safetynet.alerts.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service is responsible for retrieving phone numbers by firestation.
 */
@Service
public class PhoneAlertService {

    // DataLoader is used to access application data
    private final DataLoader dataLoader;

    // Constructor to inject the DataLoader
    public PhoneAlertService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Returns phone numbers of residents covered by a firestation
     *
     * @param stationNumber the firestation number
     * @return set of unique phone numbers
     */
    public Set<String> getPhoneNumbersByStation(int stationNumber) {

        // Get all addresses covered by the given firestation
        List<String> addresses = dataLoader.getData()
                .getFirestations()
                .stream()
                .filter(fs -> Integer.parseInt(fs.getStation()) == stationNumber)
                .map(Firestation::getAddress)
                .toList();

        // Get phone numbers of persons living at these addresses
        return dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .collect(Collectors.toSet()); // remove duplicates
    }
}