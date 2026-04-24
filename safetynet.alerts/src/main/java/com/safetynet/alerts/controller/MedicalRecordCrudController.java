package com.safetynet.alerts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordCrudService;

/**
 * This controller handles CRUD operations for medical records.
 */
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordCrudController {

    // Service used to manage medical record data
    private final MedicalRecordCrudService medicalRecordService;

    // Constructor used to inject the service
    public MedicalRecordCrudController(MedicalRecordCrudService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Adds a new medical record
     */
    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {

        // Call the service to add a medical record
        return ResponseEntity.ok(medicalRecordService.addMedicalRecord(medicalRecord));
    }

    /**
     * Updates an existing medical record
     */
    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {

        // Call the service to update the medical record
        MedicalRecord updated = medicalRecordService.updateMedicalRecord(medicalRecord);

        // If the record is not found, return HTTP 404 (Not Found)
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        // Return the updated medical record
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a medical record using first name and last name
     */
    @DeleteMapping
    public ResponseEntity<String> deleteMedicalRecord(@RequestParam String firstName,
                                                      @RequestParam String lastName) {

        // Call the service to delete the medical record
        boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);

        // If the record is not found, return HTTP 404 (Not Found)
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        // Return success message
        return ResponseEntity.ok("Medical record deleted successfully");
    }
}