package com.safetynet.alerts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FirestationResponseDTO;
import com.safetynet.alerts.service.FirestationService;

/**
 * This controller handles requests related to firestations.
 */
@RestController
public class FirestationController {

    // Service used to get people covered by a firestation
    private final FirestationService firestationService;

    // Constructor to inject the service
    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * This endpoint returns all persons covered by a firestation
     * with the number of adults and children
     * 
     * Example: /firestation?stationNumber=1
     */
    @GetMapping("/firestation")
    public FirestationResponseDTO getPersons(@RequestParam int stationNumber) {

        // Call the service to get persons by station number
        return firestationService.getPersonsByStation(stationNumber);
    }
}