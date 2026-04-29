package com.safetynet.alerts.service;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class PhoneAlertServiceTest {

    // Mock the DataLoader to avoid reading real JSON data
    @Mock
    private DataLoader dataLoader;

    // Inject the mocked DataLoader into the service
    @InjectMocks
    private PhoneAlertService phoneAlertService;

    @Test
    void shouldReturnPhoneNumbersForFirestation() {

        // Arrange: create test persons
        Person p1 = new Person();
        p1.setAddress("Address1");
        p1.setPhone("111-111-1111");

        Person p2 = new Person();
        p2.setAddress("Address2");
        p2.setPhone("222-222-2222");

        Person p3 = new Person();
        p3.setAddress("Address1");
        p3.setPhone("111-111-1111");

        // Arrange: create firestation mappings
        Firestation f1 = new Firestation();
        f1.setAddress("Address1");
        f1.setStation("1");

        Firestation f2 = new Firestation();
        f2.setAddress("Address2");
        f2.setStation("2");

        // Arrange: create fake loaded data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(p1, p2, p3));
        data.setFirestations(List.of(f1, f2));

        when(dataLoader.getData()).thenReturn(data);

        // Act
        Set<String> result = phoneAlertService.getPhoneNumbersByStation(1);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains("111-111-1111"));
    }

    @Test
    void shouldReturnEmptySetWhenNoStationMatches() {

        // Arrange
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of());
        data.setFirestations(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        Set<String> result = phoneAlertService.getPhoneNumbersByStation(99);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllDistinctPhoneNumbersForSameStation() {

        // Arrange: create test persons
        Person p1 = new Person();
        p1.setAddress("Address1");
        p1.setPhone("111-111-1111");

        Person p2 = new Person();
        p2.setAddress("Address2");
        p2.setPhone("222-222-2222");

        Person p3 = new Person();
        p3.setAddress("Address3");
        p3.setPhone("333-333-3333");

        // Arrange: create firestation mappings
        Firestation f1 = new Firestation();
        f1.setAddress("Address1");
        f1.setStation("1");

        Firestation f2 = new Firestation();
        f2.setAddress("Address2");
        f2.setStation("1");

        Firestation f3 = new Firestation();
        f3.setAddress("Address3");
        f3.setStation("2");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(p1, p2, p3));
        data.setFirestations(List.of(f1, f2, f3));

        when(dataLoader.getData()).thenReturn(data);

        // Act
        Set<String> result = phoneAlertService.getPhoneNumbersByStation(1);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains("111-111-1111"));
        assertTrue(result.contains("222-222-2222"));
    }

    @Test
    void shouldNotReturnPhoneNumberWhenPersonAddressIsNotCoveredByStation() {

        // Arrange: create test persons
        Person p1 = new Person();
        p1.setAddress("Address1");
        p1.setPhone("111-111-1111");

        Person p2 = new Person();
        p2.setAddress("Address2");
        p2.setPhone("222-222-2222");

        // Arrange: create firestation mapping
        Firestation f1 = new Firestation();
        f1.setAddress("Address1");
        f1.setStation("1");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(p1, p2));
        data.setFirestations(List.of(f1));

        when(dataLoader.getData()).thenReturn(data);

        // Act
        Set<String> result = phoneAlertService.getPhoneNumbersByStation(1);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains("111-111-1111"));
        assertFalse(result.contains("222-222-2222"));
    }

    @Test
    void shouldReturnOnlyUniquePhoneNumbersWhenSeveralPersonsShareSamePhone() {

        // Arrange: create test persons
        Person p1 = new Person();
        p1.setAddress("Address1");
        p1.setPhone("111-111-1111");

        Person p2 = new Person();
        p2.setAddress("Address1");
        p2.setPhone("111-111-1111");

        // Arrange: create firestation mapping
        Firestation f1 = new Firestation();
        f1.setAddress("Address1");
        f1.setStation("1");

        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(p1, p2));
        data.setFirestations(List.of(f1));

        when(dataLoader.getData()).thenReturn(data);

        // Act
        Set<String> result = phoneAlertService.getPhoneNumbersByStation(1);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains("111-111-1111"));
    }
}