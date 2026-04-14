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

@RestController
@RequestMapping("/firestation")
public class FirestationCrudController {

    private final FirestationCrudService firestationService;

    public FirestationCrudController(FirestationCrudService firestationService) {
        this.firestationService = firestationService;
    }

    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        return ResponseEntity.ok(firestationService.addFirestation(firestation));
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
        Firestation updated = firestationService.updateFirestation(firestation);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFirestation(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String station) {

        if (address != null) {
            boolean deleted = firestationService.deleteByAddress(address);
            return deleted ? ResponseEntity.ok("Deleted by address") : ResponseEntity.notFound().build();
        }

        if (station != null) {
            boolean deleted = firestationService.deleteByStation(station);
            return deleted ? ResponseEntity.ok("Deleted by station") : ResponseEntity.notFound().build();
        }

        return ResponseEntity.badRequest().body("Provide address or station");
    }
}