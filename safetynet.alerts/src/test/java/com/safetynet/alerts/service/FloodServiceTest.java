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

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.dto.FloodPersonDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class FloodServiceTest {

    // Mock DataLoader to avoid using real application data
    @Mock
    private DataLoader dataLoader;

    // Mock AgeCalculatorService because the service uses it
    @Mock
    private AgeCalculatorService ageCalculatorService;

    // Inject mocks into the service under test
    @InjectMocks
    private FloodService floodService;

    @Test
    void shouldReturnHouseholdsGroupedByAddressForGivenStations() {

        // Arrange: create firestation mappings
        Firestation f1 = new Firestation();
        f1.setAddress("Address1");
        f1.setStation("1");

        Firestation f2 = new Firestation();
        f2.setAddress("Address2");
        f2.setStation("2");

        Firestation f3 = new Firestation();
        f3.setAddress("Address3");
        f3.setStation("3");

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

        Person p4 = new Person();
        p4.setFirstName("Alice");
        p4.setLastName("Stone");
        p4.setAddress("Address3");
        p4.setPhone("444-444-4444");

        // Arrange: create medical records
        MedicalRecord m1 = new MedicalRecord();
        m1.setFirstName("John");
        m1.setLastName("Boyd");
        m1.setBirthdate("01/01/1980");
        m1.setMedications(List.of("med1"));
        m1.setAllergies(List.of("allergy1"));

        MedicalRecord m2 = new MedicalRecord();
        m2.setFirstName("Jane");
        m2.setLastName("Boyd");
        m2.setBirthdate("01/01/2010");
        m2.setMedications(List.of("med2"));
        m2.setAllergies(List.of("allergy2"));

        MedicalRecord m3 = new MedicalRecord();
        m3.setFirstName("Bob");
        m3.setLastName("Smith");
        m3.setBirthdate("01/01/1990");
        m3.setMedications(List.of("med3"));
        m3.setAllergies(List.of("allergy3"));

        // Arrange: create fake loaded data
        SafetyNetData data = new SafetyNetData();
        data.setFirestations(List.of(f1, f2, f3));
        data.setPersons(List.of(p1, p2, p3, p4));
        data.setMedicalrecords(List.of(m1, m2, m3));

        when(dataLoader.getData()).thenReturn(data);
        when(ageCalculatorService.calculateAge("01/01/1980")).thenReturn(45);
        when(ageCalculatorService.calculateAge("01/01/2010")).thenReturn(15);
        when(ageCalculatorService.calculateAge("01/01/1990")).thenReturn(35);

        // Act
        List<FloodAddressDTO> result = floodService.getFloodStations(List.of(1, 2));

        // Assert
        assertEquals(2, result.size());

        FloodAddressDTO address1Group = result.stream()
                .filter(group -> group.getAddress().equals("Address1"))
                .findFirst()
                .orElse(null);

        FloodAddressDTO address2Group = result.stream()
                .filter(group -> group.getAddress().equals("Address2"))
                .findFirst()
                .orElse(null);

        assertNotNull(address1Group);
        assertNotNull(address2Group);

        assertEquals(2, address1Group.getResidents().size());
        assertEquals(1, address2Group.getResidents().size());

        FloodPersonDTO john = address1Group.getResidents().stream()
                .filter(person -> person.getFirstName().equals("John"))
                .findFirst()
                .orElse(null);

        assertNotNull(john);
        assertEquals("Boyd", john.getLastName());
        assertEquals("111-111-1111", john.getPhone());
        assertEquals(45, john.getAge());
        assertEquals(List.of("med1"), john.getMedications());
        assertEquals(List.of("allergy1"), john.getAllergies());

        verify(ageCalculatorService).calculateAge("01/01/1980");
        verify(ageCalculatorService).calculateAge("01/01/2010");
        verify(ageCalculatorService).calculateAge("01/01/1990");
    }

    @Test
    void shouldReturnEmptyListWhenNoStationMatches() {

        // Arrange
        Firestation firestation = new Firestation();
        firestation.setAddress("Address1");
        firestation.setStation("3");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("Address1");
        person.setPhone("111-111-1111");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(List.of(firestation));
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        List<FloodAddressDTO> result = floodService.getFloodStations(List.of(1, 2));

        // Assert
        assertTrue(result.isEmpty());

        verify(ageCalculatorService, never()).calculateAge(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void shouldReturnDefaultValuesWhenMedicalRecordIsMissing() {

        // Arrange
        Firestation firestation = new Firestation();
        firestation.setAddress("Address1");
        firestation.setStation("1");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("Address1");
        person.setPhone("111-111-1111");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(List.of(firestation));
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        List<FloodAddressDTO> result = floodService.getFloodStations(List.of(1));

        // Assert
        assertEquals(1, result.size());
        assertEquals("Address1", result.get(0).getAddress());
        assertEquals(1, result.get(0).getResidents().size());

        FloodPersonDTO resident = result.get(0).getResidents().get(0);
        assertEquals("John", resident.getFirstName());
        assertEquals("Boyd", resident.getLastName());
        assertEquals("111-111-1111", resident.getPhone());
        assertEquals(0, resident.getAge());
        assertTrue(resident.getMedications().isEmpty());
        assertTrue(resident.getAllergies().isEmpty());

        verify(ageCalculatorService, never()).calculateAge(org.mockito.ArgumentMatchers.anyString());
    }
}