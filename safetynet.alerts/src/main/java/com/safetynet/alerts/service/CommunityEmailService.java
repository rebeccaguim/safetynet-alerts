package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service is responsible for retrieving email addresses by city.
 */
@Service
public class CommunityEmailService {

    // DataLoader is used to access application data
    private final DataLoader dataLoader;

    // Constructor to inject the DataLoader
    public CommunityEmailService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Returns all email addresses of people living in a given city
     *
     * @param city the city name
     * @return list of unique email addresses
     */
    public List<String> getEmailsByCity(String city) {

        // Get all persons from data
        return dataLoader.getData()
                .getPersons()
                .stream()

                // Keep only persons living in the given city
                .filter(person -> person.getCity().equalsIgnoreCase(city))

                // Extract email addresses
                .map(Person::getEmail)

                // Remove duplicate emails
                .distinct()

                // Convert to list
                .toList();
    }
}