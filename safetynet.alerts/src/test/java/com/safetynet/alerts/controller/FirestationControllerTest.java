package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FirestationResponseDTO;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FirestationController.class)
class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mocking the service layer to isolate controller behavior
    @MockBean
    private FirestationService firestationService;

    @Test
    void shouldReturnPersonsCoveredByStation() throws Exception {

        // Arrange: create fake DTO data returned by the service
        PersonDTO person = new PersonDTO(
                "John",
                "Doe",
                "123 Street",
                "123-456-7890"
        );

        FirestationResponseDTO response = new FirestationResponseDTO(
                List.of(person),
                1,
                0
        );

        // Mock service behavior
        when(firestationService.getPersonsByStation(1)).thenReturn(response);

        // Act & Assert: perform GET request and verify response
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons[0].firstName").value("John"))
                .andExpect(jsonPath("$.persons[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.persons[0].address").value("123 Street"))
                .andExpect(jsonPath("$.persons[0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$.numberOfAdults").value(1))
                .andExpect(jsonPath("$.numberOfChildren").value(0));
    }
}