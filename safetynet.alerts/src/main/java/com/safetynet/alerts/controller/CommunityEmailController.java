package com.safetynet.alerts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.service.CommunityEmailService;

@RestController
public class CommunityEmailController {

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {
        return communityEmailService.getEmailsByCity(city);
    }
}