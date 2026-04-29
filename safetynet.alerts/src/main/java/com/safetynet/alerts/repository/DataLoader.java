package com.safetynet.alerts.repository;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.SafetyNetData;

import jakarta.annotation.PostConstruct;

/**
 * This class is responsible for loading and saving data from the JSON file.
 */
@Repository
public class DataLoader {

    // Logger used to record application events
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    // ObjectMapper is used to convert JSON into Java objects and vice versa
    private final ObjectMapper objectMapper = new ObjectMapper();

    // This variable stores all the data loaded from the JSON file
    private SafetyNetData data;

    // This represents the JSON file on disk
    private File jsonFile;

    /**
     * Returns the loaded data
     */
    public SafetyNetData getData() {
        return data;
    }

    /**
     * This method is automatically called when the application starts.
     * It loads the data from data.json.
     */
    @PostConstruct
    public void loadData() {
        try {
            // Read the JSON file from resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json");

            // If file is not found, throw an error
            if (inputStream == null) {
                throw new IllegalStateException("data.json not found in resources");
            }

            // Convert JSON data into Java object
            data = objectMapper.readValue(inputStream, SafetyNetData.class);

            // Define the file path for saving data later
            jsonFile = new File("src/main/resources/data.json");

            // Log information to confirm data is loaded
            logger.info("Data loaded successfully");
            logger.info("Persons: {}", data.getPersons().size());
            logger.info("Firestations: {}", data.getFirestations().size());
            logger.info("Medical records: {}", data.getMedicalrecords().size());

        } catch (Exception e) {
            // Log error if loading fails
            logger.error("Failed to load data.json", e);
            throw new RuntimeException("Failed to load data.json", e);
        }
    }

    /**
     * Saves updated data back into the JSON file.
     */
    public void saveData() {
        try {
            // Write Java object into JSON file with pretty format
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, data);

            // Log success message
            logger.info("Data saved successfully");

        } catch (Exception e) {
            // Log error if saving fails
            logger.error("Failed to save data.json", e);
            throw new RuntimeException("Failed to save data.json", e);
        }
    }
}