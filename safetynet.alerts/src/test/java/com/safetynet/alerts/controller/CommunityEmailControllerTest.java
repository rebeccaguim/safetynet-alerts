package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommunityEmailController.class)
class CommunityEmailControllerTest {

    // MockMvc allows us to simulate HTTP requests without starting the server
    @Autowired
    private MockMvc mockMvc;

    // MockBean creates a mock instance of the service layer
    // This allows us to isolate the controller and control the service behavior
    @MockBean
    private CommunityEmailService communityEmailService;

    @Test
    void shouldReturnEmailsForCity() throws Exception {

        // Arrange: define the expected list of emails returned by the service
        List<String> emails = List.of(
                "test1@email.com",
                "test2@email.com"
        );

        // Mock the service behavior when called with "Culver"
        when(communityEmailService.getEmailsByCity("Culver")).thenReturn(emails);

        // Act & Assert: perform a GET request and verify the response
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver"))

                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())

                // Verify the JSON response content
                .andExpect(jsonPath("$[0]").value("test1@email.com"))
                .andExpect(jsonPath("$[1]").value("test2@email.com"));
    }
}