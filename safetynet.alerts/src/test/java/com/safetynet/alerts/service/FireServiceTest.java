package com.safetynet.alerts.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.dto.FirePersonDTO;
import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class FireServiceTest {

    // Mock DataLoader to avoid using real application data
    @Mock
    private DataLoader dataLoader;

    // Mock AgeCalculatorService because the service uses it
    @Mock
    private AgeCalculatorService ageCalculatorService;

    // Inject mocks into the service under test
    @InjectMocks
    private FireService fireService;

    @Test
    void shouldReturnResidentsAndStationNumberForAddress() {

        // Arrange: create firestation mapping
        Firestation firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("3");

        // Arrange: create person living at the address
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setPhone("841-874-6512");

        // Arrange: create medical record
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        // Arrange: create fake loaded data
        SafetyNetData data = new SafetyNetData();
        data.setFirestations(List.of(firestation));
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of(medicalRecord));

        when(dataLoader.getData()).thenReturn(data);
        when(ageCalculatorService.calculateAge("03/06/1984")).thenReturn(40);

        // Act
        FireResponseDTO result = fireService.getResidentsByAddress("1509 Culver St");

        // Assert: check station number
        assertEquals(3, result.getStationNumber());

        // Assert: check residents
        assertEquals(1, result.getResidents().size());

        FirePersonDTO resident = result.getResidents().get(0);
        assertEquals("John", resident.getFirstName());
        assertEquals("Boyd", resident.getLastName());
        assertEquals("841-874-6512", resident.getPhone());
        assertEquals(40, resident.getAge());
        assertEquals(List.of("aznol:350mg"), resident.getMedications());
        assertEquals(List.of("nillacilan"), resident.getAllergies());

        verify(ageCalculatorService).calculateAge("03/06/1984");
    }

    @Test
    void shouldReturnDefaultValuesWhenAddressDoesNotExist() {

        // Arrange: create data with no matching address
        Firestation firestation = new Firestation();
        firestation.setAddress("Other Address");
        firestation.setStation("2");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("Other Address");
        person.setPhone("841-874-6512");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(List.of(firestation));
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        FireResponseDTO result = fireService.getResidentsByAddress("1509 Culver St");

        // Assert
        assertEquals(0, result.getStationNumber());
        assertTrue(result.getResidents().isEmpty());

        verify(ageCalculatorService, never()).calculateAge(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void shouldReturnResidentWithDefaultMedicalValuesWhenMedicalRecordIsMissing() {

        // Arrange: create firestation mapping
        Firestation firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("3");

        // Arrange: create person living at the address
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setPhone("841-874-6512");

        // Arrange: no medical record
        SafetyNetData data = new SafetyNetData();
        data.setFirestations(List.of(firestation));
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        FireResponseDTO result = fireService.getResidentsByAddress("1509 Culver St");

        // Assert
        assertEquals(3, result.getStationNumber());
        assertEquals(1, result.getResidents().size());

        FirePersonDTO resident = result.getResidents().get(0);
        assertEquals("John", resident.getFirstName());
        assertEquals("Boyd", resident.getLastName());
        assertEquals("841-874-6512", resident.getPhone());
        assertEquals(0, resident.getAge());
        assertTrue(resident.getMedications().isEmpty());
        assertTrue(resident.getAllergies().isEmpty());

        verify(ageCalculatorService, never()).calculateAge(org.mockito.ArgumentMatchers.anyString());
    }
}