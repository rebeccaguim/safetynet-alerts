package com.safetynet.alerts.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.dto.FirestationResponseDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class FirestationServiceTest {

    // Mock DataLoader to avoid using real application data
    @Mock
    private DataLoader dataLoader;

    // Mock AgeCalculatorService because it is used to determine if a person is a child
    @Mock
    private AgeCalculatorService ageCalculatorService;

    // Inject mocks into the service under test
    @InjectMocks
    private FirestationService firestationService;

    @Test
    void shouldReturnPersonsCoveredByStationWithAdultAndChildCount() {

        // Arrange: create persons
        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Boyd");
        p1.setAddress("Address1");
        p1.setPhone("111-111-1111");

        Person p2 = new Person();
        p2.setFirstName("Jane");
        p2.setLastName("Boyd");
        p2.setAddress("Address1");
        p2.setPhone("222-222-2222");

        Person p3 = new Person();
        p3.setFirstName("Bob");
        p3.setLastName("Smith");
        p3.setAddress("Address2");
        p3.setPhone("333-333-3333");

        // Arrange: create firestation mappings
        Firestation f1 = new Firestation();
        f1.setAddress("Address1");
        f1.setStation("1");

        Firestation f2 = new Firestation();
        f2.setAddress("Address2");
        f2.setStation("2");

        // Arrange: create medical records
        MedicalRecord m1 = new MedicalRecord();
        m1.setFirstName("John");
        m1.setLastName("Boyd");
        m1.setBirthdate("01/01/2015");

        MedicalRecord m2 = new MedicalRecord();
        m2.setFirstName("Jane");
        m2.setLastName("Boyd");
        m2.setBirthdate("01/01/1980");

        // Arrange: create fake loaded data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(p1, p2, p3));
        data.setFirestations(List.of(f1, f2));
        data.setMedicalrecords(List.of(m1, m2));

        when(dataLoader.getData()).thenReturn(data);
        when(ageCalculatorService.isChild("01/01/2015")).thenReturn(true);
        when(ageCalculatorService.isChild("01/01/1980")).thenReturn(false);

        // Act
        FirestationResponseDTO result = firestationService.getPersonsByStation(1);

        // Assert: check persons list
        assertEquals(2, result.getPersons().size());

        PersonDTO firstPerson = result.getPersons().get(0);
        assertNotNull(firstPerson);

        // Assert: check adult and child count
        assertEquals(1, result.getNumberOfAdults());
        assertEquals(1, result.getNumberOfChildren());

        // Verify age service calls
        verify(ageCalculatorService).isChild("01/01/2015");
        verify(ageCalculatorService).isChild("01/01/1980");
    }

    @Test
    void shouldReturnEmptyResponseWhenNoStationMatches() {

        // Arrange: create data with no matching station
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("Address1");
        person.setPhone("111-111-1111");

        Firestation firestation = new Firestation();
        firestation.setAddress("Address1");
        firestation.setStation("2");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(person));
        data.setFirestations(List.of(firestation));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        FirestationResponseDTO result = firestationService.getPersonsByStation(1);

        // Assert
        assertTrue(result.getPersons().isEmpty());
        assertEquals(0, result.getNumberOfAdults());
        assertEquals(0, result.getNumberOfChildren());

        // Verify that age service was never called
        verify(ageCalculatorService, never()).isChild(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void shouldCountPersonAsAdultWhenMedicalRecordIsMissing() {

        // Arrange: create a person
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("Address1");
        person.setPhone("111-111-1111");

        // Arrange: create matching firestation
        Firestation firestation = new Firestation();
        firestation.setAddress("Address1");
        firestation.setStation("1");

        // Arrange: no medical record
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(person));
        data.setFirestations(List.of(firestation));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        FirestationResponseDTO result = firestationService.getPersonsByStation(1);

        // Assert
        assertEquals(1, result.getPersons().size());
        assertEquals(1, result.getNumberOfAdults());
        assertEquals(0, result.getNumberOfChildren());

        // Verify that age service was never called because no record was found
        verify(ageCalculatorService, never()).isChild(org.mockito.ArgumentMatchers.anyString());
    }
}