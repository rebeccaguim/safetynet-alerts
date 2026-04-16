package com.safetynet.alerts.service;

// Import List to store multiple elements
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

// Enable Mockito in this test class
@ExtendWith(MockitoExtension.class)
class CommunityEmailServiceTest {

    // Create a fake DataLoader (mock object)
    // This avoids using real data
    @Mock
    private DataLoader dataLoader;

    // Inject the mock into the service
    // We only test the service logic
    @InjectMocks
    private CommunityEmailService communityEmailService;

    // Test: should return unique emails for a given city
    @Test
    void shouldReturnDistinctEmailsForCity() {

        // Create first person
        Person p1 = new Person();
        p1.setCity("Culver");
        p1.setEmail("a@email.com");

        // Create second person with different email
        Person p2 = new Person();
        p2.setCity("Culver");
        p2.setEmail("b@email.com");

        // Create third person with duplicate email
        Person p3 = new Person();
        p3.setCity("Culver");
        p3.setEmail("a@email.com");

        // Create a person in another city
        Person p4 = new Person();
        p4.setCity("Paris");
        p4.setEmail("c@email.com");

        // Create test data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(p1, p2, p3, p4));

        // Mock behavior
        when(dataLoader.getData()).thenReturn(data);

        // Call the method to test
        List<String> result = communityEmailService.getEmailsByCity("Culver");

        // Check that there are only 2 unique emails
        assertEquals(2, result.size());

        // Check that expected emails are present
        assertTrue(result.contains("a@email.com"));
        assertTrue(result.contains("b@email.com"));
    }

    // Test: should return empty list if no person matches the city
    @Test
    void shouldReturnEmptyListWhenNoPersonMatchesCity() {

        // Create a person in another city
        Person p1 = new Person();
        p1.setCity("Paris");
        p1.setEmail("a@email.com");

        // Create test data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(p1));

        // Mock behavior
        when(dataLoader.getData()).thenReturn(data);

        // Call the method with a city that does not exist
        List<String> result = communityEmailService.getEmailsByCity("Culver");

        // Check that the result is empty
        assertTrue(result.isEmpty());
    }

    // Test: should ignore case when searching by city
    @Test
    void shouldReturnEmailsIgnoringCityCase() {

        // Create a person with city name in lowercase
        Person p1 = new Person();
        p1.setCity("culver");
        p1.setEmail("a@email.com");

        // Create test data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(p1));

        // Mock behavior
        when(dataLoader.getData()).thenReturn(data);

        // Call the method with a different letter case
        List<String> result = communityEmailService.getEmailsByCity("CulVer");

        // Check that the email is returned
        assertEquals(1, result.size());
        assertTrue(result.contains("a@email.com"));
    }

    // Test: should return empty list when there are no persons
    @Test
    void shouldReturnEmptyListWhenPersonsListIsEmpty() {

        // Create test data with an empty persons list
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of());

        // Mock behavior
        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        List<String> result = communityEmailService.getEmailsByCity("Culver");

        // Check that the result is empty
        assertTrue(result.isEmpty());
    }
}