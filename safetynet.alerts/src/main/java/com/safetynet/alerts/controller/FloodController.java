package com.safetynet.alerts.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.service.FloodService;

@RestController
public class FloodController {

    private final FloodService floodService;

    public FloodController(FloodService floodService) {
        this.floodService = floodService;
    }

    @GetMapping("/flood/stations")
    public List<FloodAddressDTO> getFloodStations(@RequestParam String stations) {
        List<Integer> stationNumbers = Arrays.stream(stations.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        return floodService.getFloodStations(stationNumbers);
    }
}