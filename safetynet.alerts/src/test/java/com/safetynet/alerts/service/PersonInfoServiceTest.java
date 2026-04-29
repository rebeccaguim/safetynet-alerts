package com.safetynet.alerts.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class PersonInfoServiceTest {

    // Mock DataLoader to avoid using real application data
    @Mock
    private DataLoader dataLoader;

    // Mock AgeCalculatorService because it is a dependency of PersonInfoService
    @Mock
    private AgeCalculatorService ageCalculatorService;

    // Inject mocks into the service under test
    @InjectMocks
    private PersonInfoService personInfoService;

    @Test
    void shouldReturnPersonInfoWhenLastNameMatches() {

        // Arrange: create a person
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setEmail("john.boyd@email.com");

        // Arrange: create matching medical record
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        // Arrange: create fake loaded data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of(medicalRecord));

        when(dataLoader.getData()).thenReturn(data);
        when(ageCalculatorService.calculateAge("03/06/1984")).thenReturn(40);

        // Act
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Boyd");

        // Assert
        assertEquals(1, result.size());

        PersonInfoDTO dto = result.get(0);
        assertEquals("John", dto.getFirstName());
        assertEquals("Boyd", dto.getLastName());
        assertEquals("1509 Culver St", dto.getAddress());
        assertEquals(40, dto.getAge());
        assertEquals("john.boyd@email.com", dto.getEmail());
        assertEquals(List.of("aznol:350mg"), dto.getMedications());
        assertEquals(List.of("nillacilan"), dto.getAllergies());
    }

    @Test
    void shouldReturnEmptyListWhenNoLastNameMatches() {

        // Arrange: create a person with another last name
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAddress("1509 Culver St");
        person.setEmail("john.smith@email.com");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Boyd");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnDefaultValuesWhenMedicalRecordIsMissing() {

        // Arrange: create a person
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setEmail("john.boyd@email.com");

        // Arrange: no matching medical record
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Boyd");

        // Assert
        assertEquals(1, result.size());

        PersonInfoDTO dto = result.get(0);
        assertEquals("John", dto.getFirstName());
        assertEquals("Boyd", dto.getLastName());
        assertEquals("1509 Culver St", dto.getAddress());
        assertEquals(0, dto.getAge());
        assertEquals("john.boyd@email.com", dto.getEmail());
        assertTrue(dto.getMedications().isEmpty());
        assertTrue(dto.getAllergies().isEmpty());
    }
}