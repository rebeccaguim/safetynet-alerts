package com.safetynet.alerts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonInfoService;

/**
 * This controller handles requests to get detailed information about persons.
 */
@RestController
public class PersonInfoController {

    // Service used to retrieve person information
    private final PersonInfoService personInfoService;

    // Constructor used to inject the service
    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    /**
     * Returns information about persons with the given last name
     * 
     * Example: /personInfo?lastName=Boyd
     */
    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam String lastName) {

        // Call the service to get person information by last name
        return personInfoService.getPersonInfoByLastName(lastName);
    }
}