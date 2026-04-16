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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class FirestationCrudServiceTest {

    @Mock
    // This creates a fake DataLoader
    private DataLoader dataLoader;

    @InjectMocks
    // This creates the service and injects the fake DataLoader into it
    private FirestationCrudService firestationCrudService;

    @Test
    void shouldAddFirestation() {
        // Create an empty data object
        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>());

        // Create the firestation to add
        Firestation firestation = new Firestation();
        firestation.setAddress("123 Test St");
        firestation.setStation("5");

        // Tell the mock what to return
        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        Firestation result = firestationCrudService.addFirestation(firestation);

        // Check the result
        assertNotNull(result);
        assertEquals("123 Test St", result.getAddress());
        assertEquals("5", result.getStation());
        assertEquals(1, data.getFirestations().size());

        // Check that saveData was called
        verify(dataLoader, times(1)).saveData();
    }

    @Test
    void shouldUpdateFirestationWhenAddressExists() {
        // Create existing firestation data
        Firestation existing = new Firestation();
        existing.setAddress("123 Test St");
        existing.setStation("1");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(existing)));

        // Create updated firestation with same address but new station
        Firestation updated = new Firestation();
        updated.setAddress("123 Test St");
        updated.setStation("3");

        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        Firestation result = firestationCrudService.updateFirestation(updated);

        // Check the result
        assertNotNull(result);
        assertEquals("3", result.getStation());
        assertEquals("3", data.getFirestations().get(0).getStation());

        // Check that saveData was called
        verify(dataLoader, times(1)).saveData();
    }

    @Test
    void shouldReturnNullWhenUpdatingUnknownAddress() {
        // Create data with one firestation
        Firestation existing = new Firestation();
        existing.setAddress("123 Test St");
        existing.setStation("1");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(existing)));

        // Create an update for another address
        Firestation updated = new Firestation();
        updated.setAddress("999 Unknown St");
        updated.setStation("4");

        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        Firestation result = firestationCrudService.updateFirestation(updated);

        // The result must be null because the address does not exist
        assertNull(result);

        // saveData must not be called
        verify(dataLoader, never()).saveData();
    }

    @Test
    void shouldDeleteFirestationByAddress() {
        // Create data with two firestations
        Firestation f1 = new Firestation();
        f1.setAddress("123 Test St");
        f1.setStation("1");

        Firestation f2 = new Firestation();
        f2.setAddress("456 Other St");
        f2.setStation("2");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(f1, f2)));

        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        boolean deleted = firestationCrudService.deleteByAddress("123 Test St");

        // Check that one item was deleted
        assertTrue(deleted);
        assertEquals(1, data.getFirestations().size());
        assertEquals("456 Other St", data.getFirestations().get(0).getAddress());

        // Check that saveData was called
        verify(dataLoader, times(1)).saveData();
    }

    @Test
    void shouldReturnFalseWhenDeletingUnknownAddress() {
        // Create data with one firestation
        Firestation f1 = new Firestation();
        f1.setAddress("123 Test St");
        f1.setStation("1");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(f1)));

        when(dataLoader.getData()).thenReturn(data);

        // Call the method with an unknown address
        boolean deleted = firestationCrudService.deleteByAddress("999 Unknown St");

        // Nothing should be deleted
        assertFalse(deleted);
        assertEquals(1, data.getFirestations().size());

        // saveData must not be called
        verify(dataLoader, never()).saveData();
    }

    @Test
    void shouldDeleteFirestationByStation() {
        // Create data with two firestations using different stations
        Firestation f1 = new Firestation();
        f1.setAddress("123 Test St");
        f1.setStation("1");

        Firestation f2 = new Firestation();
        f2.setAddress("456 Other St");
        f2.setStation("2");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(f1, f2)));

        when(dataLoader.getData()).thenReturn(data);

        // Call the method
        boolean deleted = firestationCrudService.deleteByStation("2");

        // Check that the station was deleted
        assertTrue(deleted);
        assertEquals(1, data.getFirestations().size());
        assertEquals("1", data.getFirestations().get(0).getStation());

        // Check that saveData was called
        verify(dataLoader, times(1)).saveData();
    }

    @Test
    void shouldReturnFalseWhenDeletingUnknownStation() {
        // Create data with one firestation
        Firestation f1 = new Firestation();
        f1.setAddress("123 Test St");
        f1.setStation("1");

        SafetyNetData data = new SafetyNetData();
        data.setFirestations(new ArrayList<>(List.of(f1)));

        when(dataLoader.getData()).thenReturn(data);

        // Call the method with an unknown station
        boolean deleted = firestationCrudService.deleteByStation("99");

        // Nothing should be deleted
        assertFalse(deleted);
        assertEquals(1, data.getFirestations().size());

        // saveData must not be called
        verify(dataLoader, never()).saveData();
    }
}