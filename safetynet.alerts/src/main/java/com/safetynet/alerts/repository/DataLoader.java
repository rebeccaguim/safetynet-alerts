package com.safetynet.alerts.repository;

import java.io.InputStream;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.SafetyNetData;

import jakarta.annotation.PostConstruct;

@Repository
public class DataLoader {

    private SafetyNetData data;

    public SafetyNetData getData() {
        return data;
    }

    @PostConstruct
    public void loadData() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("data.json");

            if (inputStream == null) {
                throw new IllegalStateException("data.json not found in resources");
            }

            data = mapper.readValue(inputStream, SafetyNetData.class);

            System.out.println("Data loaded successfully");
            System.out.println("Persons: " + data.getPersons().size());
            System.out.println("Firestations: " + data.getFirestations().size());
            System.out.println("Medical records: " + data.getMedicalrecords().size());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load data.json", e);
        }
    }
}