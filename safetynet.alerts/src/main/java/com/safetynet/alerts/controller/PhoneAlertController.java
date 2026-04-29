package com.safetynet.alerts.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.service.PhoneAlertService;

/**
 * This controller handles requests related to phone alerts.
 */
@RestController
public class PhoneAlertController {

    // Service used to retrieve phone numbers
    private final PhoneAlertService phoneAlertService;

    // Constructor used to inject the service
    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    /**
     * Returns phone numbers of residents covered by a firestation
     * 
     * Example: /phoneAlert?firestation=1
     */
    @GetMapping("/phoneAlert")
    public Set<String> getPhones(@RequestParam int firestation) {

        // Call the service to get phone numbers by station
        return phoneAlertService.getPhoneNumbersByStation(firestation);
    }
}