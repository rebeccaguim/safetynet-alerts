package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

/**
 * This service is responsible for calculating age from a birthdate.
 */
@Service
public class AgeCalculatorService {

    // Formatter used to parse date from String (MM/dd/yyyy format)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Calculates the age from a birthdate string
     *
     * @param birthdate the birthdate in format MM/dd/yyyy
     * @return the calculated age
     */
    public int calculateAge(String birthdate) {

        // Convert the String birthdate into a LocalDate object
        LocalDate birthDate = LocalDate.parse(birthdate, FORMATTER);

        // Calculate the difference between birthdate and today
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Checks if a person is a child (18 years old or younger)
     *
     * @param birthdate the birthdate in format MM/dd/yyyy
     * @return true if the person is a child, false otherwise
     */
    public boolean isChild(String birthdate) {

        // A child is defined as age less than or equal to 18
        return calculateAge(birthdate) <= 18;
    }
}