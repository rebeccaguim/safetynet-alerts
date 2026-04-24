package com.safetynet.alerts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.service.FireService;

/**
 * This controller handles requests related to fire emergencies.
 */
@RestController
public class FireController {

    // Service used to get residents information by address
    private final FireService fireService;

    // Constructor to inject the service
    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    /**
     * This endpoint returns residents living at a given address
     * with their age, phone and medical information
     * 
     * Example: /fire?address=1509 Culver St
     */
    @GetMapping("/fire")
    public FireResponseDTO getResidents(@RequestParam String address) {

        // Call the service to get residents by address
        return fireService.getResidentsByAddress(address);
    }
}