package com.safetynet.alerts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.service.CommunityEmailService;

/**
 * This controller handles requests to get email addresses of people in a city.
 */
@RestController
public class CommunityEmailController {

    // Service used to retrieve email addresses
    private final CommunityEmailService communityEmailService;

    // Constructor to inject the service
    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    /**
     * This endpoint returns all email addresses for a given city
     * 
     * Example: /communityEmail?city=Culver
     */
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {

        // Call the service to get emails by city
        return communityEmailService.getEmailsByCity(city);
    }
}