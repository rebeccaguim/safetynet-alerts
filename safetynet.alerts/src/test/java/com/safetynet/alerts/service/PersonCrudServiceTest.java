package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class PersonCrudServiceTest {

    @Mock
    // This creates a fake DataLoader
    private DataLoader dataLoader;

    @InjectMocks
    // This creates the service and injects the fake DataLoader into it
    private PersonCrudService personCrudService;

    @Test
    void shouldAddPerson() {
        // Create an empty data object
        SafetyNetData data = new SafetyNetData();
        data.setPersons(new ArrayList<>());

        // Create the person to add
        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setAddress("123 Test St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("123-456-7890");
        person.setEmail("jane.doe@email.com");

        // Tell the mock what to return
        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        Person result = personCrudService.addPerson(person);

        // Check the result
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(1, data.getPersons().size());

        // Check that saveData was called
        verify(dataLoader, times(1)).saveData();
    }

    @Test
    void shouldUpdatePersonWhenPersonExists() {
        // Create existing person
        Person existing = new Person();
        existing.setFirstName("John");
        existing.setLastName("Doe");
        existing.setAddress("Old Address");
        existing.setCity("Old City");
        existing.setZip("00000");
        existing.setPhone("000-000-0000");
        existing.setEmail("old@email.com");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(new ArrayList<>(List.of(existing)));

        // Create updated person with same first name and last name
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");
        updatedPerson.setAddress("New Address");
        updatedPerson.setCity("New City");
        updatedPerson.setZip("11111");
        updatedPerson.setPhone("111-111-1111");
        updatedPerson.setEmail("new@email.com");

        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        Person result = personCrudService.updatePerson(updatedPerson);

        // Check the result
        assertNotNull(result);
        assertEquals("New Address", result.getAddress());
        assertEquals("New City", result.getCity());
        assertEquals("11111", result.getZip());
        assertEquals("111-111-1111", result.getPhone());
        assertEquals("new@email.com", result.getEmail());

        // Check that saveData was called
        verify(dataLoader, times(1)).saveData();
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPersonThatDoesNotExist() {
        // Create existing person
        Person existing = new Person();
        existing.setFirstName("John");
        existing.setLastName("Doe");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(new ArrayList<>(List.of(existing)));

        // Create person that does not exist
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Jane");
        updatedPerson.setLastName("Doe");

        when(dataLoader.getData()).thenReturn(data);

        // Check that an exception is thrown because the person does not exist
        assertThrows(ResourceNotFoundException.class, () ->
                personCrudService.updatePerson(updatedPerson)
        );

        // saveData must not be called
        verify(dataLoader, never()).saveData();
    }

    @Test
    void shouldDeletePersonWhenPersonExists() {
        // Create existing persons
        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Doe");

        Person p2 = new Person();
        p2.setFirstName("Jane");
        p2.setLastName("Doe");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(new ArrayList<>(List.of(p1, p2)));

        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        boolean deleted = personCrudService.deletePerson("Jane", "Doe");

        // Check that the person was deleted
        assertTrue(deleted);
        assertEquals(1, data.getPersons().size());
        assertEquals("John", data.getPersons().get(0).getFirstName());

        // Check that saveData was called
        verify(dataLoader, times(1)).saveData();
    }

    @Test
    void shouldThrowExceptionWhenDeletingPersonThatDoesNotExist() {
        // Create existing person
        Person existing = new Person();
        existing.setFirstName("John");
        existing.setLastName("Doe");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(new ArrayList<>(List.of(existing)));

        when(dataLoader.getData()).thenReturn(data);

        // Check that an exception is thrown because the person does not exist
        assertThrows(ResourceNotFoundException.class, () ->
                personCrudService.deletePerson("Jane", "Doe")
        );

        // saveData must not be called
        verify(dataLoader, never()).saveData();
    }
}