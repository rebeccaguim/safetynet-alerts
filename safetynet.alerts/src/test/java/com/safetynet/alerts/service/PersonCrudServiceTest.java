package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class PersonCrudServiceTest {

    // Mock the DataLoader to avoid using real application data
    @Mock
    private DataLoader dataLoader;

    // Inject the mock into the service
    @InjectMocks
    private PersonCrudService personCrudService;

    @Test
    void shouldAddPerson() {

        // Arrange: create an empty persons list
        List<Person> persons = new ArrayList<>();

        // Arrange: create SafetyNetData and set the persons list
        SafetyNetData data = new SafetyNetData();
        data.setPersons(persons);

        // Arrange: create the person to add
        Person newPerson = new Person();
        newPerson.setFirstName("John");
        newPerson.setLastName("Boyd");
        newPerson.setAddress("1509 Culver St");
        newPerson.setCity("Culver");
        newPerson.setZip("97451");
        newPerson.setPhone("841-874-6512");
        newPerson.setEmail("john.boyd@email.com");

        // Mock DataLoader behavior
        when(dataLoader.getData()).thenReturn(data);

        // Act: call the service method
        Person result = personCrudService.addPerson(newPerson);

        // Assert: check that the person was added
        assertEquals(1, persons.size());
        assertTrue(persons.contains(newPerson));
        assertEquals(newPerson, result);

        // Verify that saveData() was called
        verify(dataLoader).saveData();
    }

    @Test
    void shouldUpdatePersonWhenPersonExists() {

        // Arrange: create an existing person
        Person existingPerson = new Person();
        existingPerson.setFirstName("John");
        existingPerson.setLastName("Boyd");
        existingPerson.setAddress("Old Address");
        existingPerson.setCity("Old City");
        existingPerson.setZip("00000");
        existingPerson.setPhone("000-000-0000");
        existingPerson.setEmail("old@email.com");

        // Arrange: create the updated person with new information
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Boyd");
        updatedPerson.setAddress("New Address");
        updatedPerson.setCity("New City");
        updatedPerson.setZip("11111");
        updatedPerson.setPhone("111-111-1111");
        updatedPerson.setEmail("new@email.com");

        // Arrange: create the persons list
        List<Person> persons = new ArrayList<>();
        persons.add(existingPerson);

        // Arrange: create SafetyNetData
        SafetyNetData data = new SafetyNetData();
        data.setPersons(persons);

        // Mock DataLoader behavior
        when(dataLoader.getData()).thenReturn(data);

        // Act: call the service method
        Person result = personCrudService.updatePerson(updatedPerson);

        // Assert: check that the person was updated
        assertNotNull(result);
        assertEquals("New Address", result.getAddress());
        assertEquals("New City", result.getCity());
        assertEquals("11111", result.getZip());
        assertEquals("111-111-1111", result.getPhone());
        assertEquals("new@email.com", result.getEmail());

        // Verify that saveData() was called
        verify(dataLoader).saveData();
    }

    @Test
    void shouldReturnNullWhenUpdatingPersonThatDoesNotExist() {

        // Arrange: create an existing person
        Person existingPerson = new Person();
        existingPerson.setFirstName("John");
        existingPerson.setLastName("Boyd");

        // Arrange: create a different person to update
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Jane");
        updatedPerson.setLastName("Doe");
        updatedPerson.setAddress("New Address");
        updatedPerson.setCity("New City");
        updatedPerson.setZip("11111");
        updatedPerson.setPhone("111-111-1111");
        updatedPerson.setEmail("new@email.com");

        // Arrange: create the persons list
        List<Person> persons = new ArrayList<>();
        persons.add(existingPerson);

        // Arrange: create SafetyNetData
        SafetyNetData data = new SafetyNetData();
        data.setPersons(persons);

        // Mock DataLoader behavior
        when(dataLoader.getData()).thenReturn(data);

        // Act: call the service method
        Person result = personCrudService.updatePerson(updatedPerson);

        // Assert: check that no person was updated
        assertNull(result);

        // Verify that saveData() was not called
        verify(dataLoader, never()).saveData();
    }

    @Test
    void shouldDeletePersonWhenPersonExists() {

        // Arrange: create a person
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");

        // Arrange: create the persons list
        List<Person> persons = new ArrayList<>();
        persons.add(person);

        // Arrange: create SafetyNetData
        SafetyNetData data = new SafetyNetData();
        data.setPersons(persons);

        // Mock DataLoader behavior
        when(dataLoader.getData()).thenReturn(data);

        // Act: call the service method
        boolean result = personCrudService.deletePerson("John", "Boyd");

        // Assert: check that the person was deleted
        assertTrue(result);
        assertTrue(persons.isEmpty());

        // Verify that saveData() was called
        verify(dataLoader).saveData();
    }

    @Test
    void shouldReturnFalseWhenDeletingPersonThatDoesNotExist() {

        // Arrange: create a person
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");

        // Arrange: create the persons list
        List<Person> persons = new ArrayList<>();
        persons.add(person);

        // Arrange: create SafetyNetData
        SafetyNetData data = new SafetyNetData();
        data.setPersons(persons);

        // Mock DataLoader behavior
        when(dataLoader.getData()).thenReturn(data);

        // Act: call the service method
        boolean result = personCrudService.deletePerson("Jane", "Doe");

        // Assert: check that no person was deleted
        assertFalse(result);
        assertEquals(1, persons.size());

        // Verify that saveData() was not called
        verify(dataLoader, never()).saveData();
    }
}