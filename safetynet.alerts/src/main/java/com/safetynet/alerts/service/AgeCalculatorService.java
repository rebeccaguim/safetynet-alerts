package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class AgeCalculatorService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate, FORMATTER);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public boolean isChild(String birthdate) {
        return calculateAge(birthdate) <= 18;
    }
}