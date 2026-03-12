package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class CommunityEmailService {

    private final DataLoader dataLoader;

    public CommunityEmailService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public List<String> getEmailsByCity(String city) {
        return dataLoader.getData()
                .getPersons()
                .stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .toList();
    }
}