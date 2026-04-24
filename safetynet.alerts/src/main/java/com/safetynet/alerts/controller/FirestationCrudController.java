package com.safetynet.alerts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationCrudService;

/**
 * This controller manages CRUD operations for firestations.
 */
@RestController
@RequestMapping("/firestation")
public class FirestationCrudController {

    // Service used to manage firestation data
    private final FirestationCrudService firestationService;

    // Constructor to inject the service
    public FirestationCrudController(FirestationCrudService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Add a new firestation
     */
    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {

        // Call the service to add a new firestation
        return ResponseEntity.ok(firestationService.addFirestation(firestation));
    }

    /**
     * Update an existing firestation
     */
    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {

        // Call the service to update firestation
        Firestation updated = firestationService.updateFirestation(firestation);

        // If not found, return 404
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        // Return updated firestation
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a firestation by address or station number
     */
    @DeleteMapping
    public ResponseEntity<String> deleteFirestation(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String station) {

        // Delete by address if provided
        if (address != null) {
            boolean deleted = firestationService.deleteByAddress(address);
            return deleted ? ResponseEntity.ok("Deleted by address") : ResponseEntity.notFound().build();
        }

        // Delete by station if provided
        if (station != null) {
            boolean deleted = firestationService.deleteByStation(station);
            return deleted ? ResponseEntity.ok("Deleted by station") : ResponseEntity.notFound().build();
        }

        // If no parameter is provided, return bad request
        return ResponseEntity.badRequest().body("Provide address or station");
    }
}