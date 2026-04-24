package com.safetynet.alerts.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.service.FloodService;

/**
 * This controller handles requests related to flood stations.
 */
@RestController
public class FloodController {

    // Service used to get flood-related data
    private final FloodService floodService;

    // Constructor used to inject the service
    public FloodController(FloodService floodService) {
        this.floodService = floodService;
    }

    /**
     * Returns households covered by a list of firestations
     * 
     * Example: /flood/stations?stations=1,2,3
     */
    @GetMapping("/flood/stations")
    public List<FloodAddressDTO> getFloodStations(@RequestParam String stations) {

        // Convert the input string "1,2,3" into a list of integers [1, 2, 3]
        List<Integer> stationNumbers = Arrays.stream(stations.split(","))
                .map(String::trim)           // remove spaces
                .map(Integer::parseInt)      // convert to Integer
                .toList();

        // Call the service with the list of station numbers
        return floodService.getFloodStations(stationNumbers);
    }
}