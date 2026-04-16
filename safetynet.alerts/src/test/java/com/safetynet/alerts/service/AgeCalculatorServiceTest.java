package com.safetynet.alerts.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class AgeCalculatorServiceTest {

    // Create the service instance to test business logic
    private final AgeCalculatorService ageCalculatorService = new AgeCalculatorService();

    @Test
    void shouldCalculateAgeFromBirthdate() {
        // Act: calculate the age from a known birthdate
        int age = ageCalculatorService.calculateAge("03/06/2000");

        // Assert: age should be greater than 0
        assertTrue(age > 0);
    }

    @Test
    void shouldReturnTrueWhenPersonIsChild() {
        // Act: check if a recent birthdate is considered a child
        boolean isChild = ageCalculatorService.isChild("03/06/2015");

        // Assert
        assertTrue(isChild);
    }

    @Test
    void shouldReturnFalseWhenPersonIsAdult() {
        // Act: check if an older birthdate is considered an adult
        boolean isChild = ageCalculatorService.isChild("03/06/1984");

        // Assert
        assertFalse(isChild);
    }
}