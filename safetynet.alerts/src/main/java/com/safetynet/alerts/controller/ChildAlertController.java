package com.safetynet.alerts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.ChildDTO;
import com.safetynet.alerts.service.ChildAlertService;

/**
 * This controller handles requests related to child alerts.
 */
@RestController
public class ChildAlertController {

    // Service used to get children information
    private final ChildAlertService childAlertService;

    // Constructor to inject the service
    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    /**
     * This endpoint returns the list of children living at a given address
     * 
     * Example: /childAlert?address=1509 Culver St
     */
    @GetMapping("/childAlert")
    public List<ChildDTO> getChildren(@RequestParam String address) {

        // Call the service to get children by address
        return childAlertService.getChildrenByAddress(address);
    }
}