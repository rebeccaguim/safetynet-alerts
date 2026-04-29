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

import com.safetynet.alerts.dto.ChildDTO;
import com.safetynet.alerts.dto.HouseholdMemberDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import com.safetynet.alerts.repository.DataLoader;

@ExtendWith(MockitoExtension.class)
class ChildAlertServiceTest {

    // Mock DataLoader to avoid using real application data
    @Mock
    private DataLoader dataLoader;

    // Mock AgeCalculatorService because the service uses it
    @Mock
    private AgeCalculatorService ageCalculatorService;

    // Inject mocks into the service under test
    @InjectMocks
    private ChildAlertService childAlertService;

    @Test
    void shouldReturnChildrenAtAddressWithHouseholdMembers() {

        // Arrange: create child
        Person child = new Person();
        child.setFirstName("John");
        child.setLastName("Boyd");
        child.setAddress("1509 Culver St");

        // Arrange: create adult living at same address
        Person adult = new Person();
        adult.setFirstName("Jane");
        adult.setLastName("Boyd");
        adult.setAddress("1509 Culver St");

        // Arrange: create person at another address
        Person otherPerson = new Person();
        otherPerson.setFirstName("Bob");
        otherPerson.setLastName("Smith");
        otherPerson.setAddress("Other Address");

        // Arrange: create medical records
        MedicalRecord childRecord = new MedicalRecord();
        childRecord.setFirstName("John");
        childRecord.setLastName("Boyd");
        childRecord.setBirthdate("01/01/2015");

        MedicalRecord adultRecord = new MedicalRecord();
        adultRecord.setFirstName("Jane");
        adultRecord.setLastName("Boyd");
        adultRecord.setBirthdate("01/01/1980");

        // Arrange: create fake loaded data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(child, adult, otherPerson));
        data.setMedicalrecords(List.of(childRecord, adultRecord));

        when(dataLoader.getData()).thenReturn(data);
        when(ageCalculatorService.isChild("01/01/2015")).thenReturn(true);
        when(ageCalculatorService.calculateAge("01/01/2015")).thenReturn(10);
        when(ageCalculatorService.isChild("01/01/1980")).thenReturn(false);

        // Act
        List<ChildDTO> result = childAlertService.getChildrenByAddress("1509 Culver St");

        // Assert
        assertEquals(1, result.size());

        ChildDTO childDTO = result.get(0);
        assertEquals("John", childDTO.getFirstName());
        assertEquals("Boyd", childDTO.getLastName());
        assertEquals(10, childDTO.getAge());

        assertEquals(1, childDTO.getHouseholdMembers().size());
        HouseholdMemberDTO householdMember = childDTO.getHouseholdMembers().get(0);
        assertEquals("Jane", householdMember.getFirstName());
        assertEquals("Boyd", householdMember.getLastName());

        verify(ageCalculatorService).isChild("01/01/2015");
        verify(ageCalculatorService).calculateAge("01/01/2015");
        verify(ageCalculatorService).isChild("01/01/1980");
    }

    @Test
    void shouldReturnEmptyListWhenNoChildLivesAtAddress() {

        // Arrange: create adult person
        Person adult = new Person();
        adult.setFirstName("Jane");
        adult.setLastName("Boyd");
        adult.setAddress("1509 Culver St");

        // Arrange: create adult medical record
        MedicalRecord adultRecord = new MedicalRecord();
        adultRecord.setFirstName("Jane");
        adultRecord.setLastName("Boyd");
        adultRecord.setBirthdate("01/01/1980");

        // Arrange: create fake loaded data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(adult));
        data.setMedicalrecords(List.of(adultRecord));

        when(dataLoader.getData()).thenReturn(data);
        when(ageCalculatorService.isChild("01/01/1980")).thenReturn(false);

        // Act
        List<ChildDTO> result = childAlertService.getChildrenByAddress("1509 Culver St");

        // Assert
        assertTrue(result.isEmpty());

        verify(ageCalculatorService).isChild("01/01/1980");
        verify(ageCalculatorService, never()).calculateAge("01/01/1980");
    }

    @Test
    void shouldIgnorePersonWhenMedicalRecordIsMissing() {

        // Arrange: create person without medical record
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");

        // Arrange: create fake loaded data
        SafetyNetData data = new SafetyNetData();
        data.setPersons(List.of(person));
        data.setMedicalrecords(List.of());

        when(dataLoader.getData()).thenReturn(data);

        // Act
        List<ChildDTO> result = childAlertService.getChildrenByAddress("1509 Culver St");

        // Assert
        assertTrue(result.isEmpty());

        verify(ageCalculatorService, never()).isChild(org.mockito.ArgumentMatchers.anyString());
        verify(ageCalculatorService, never()).calculateAge(org.mockito.ArgumentMatchers.anyString());
    }
}