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
import com.safetynet.alerts.service.MedicalRecordService;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return ResponseEntity.ok(medicalRecordService.addMedicalRecord(medicalRecord));
    }

    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord updated = medicalRecordService.updateMedicalRecord(medicalRecord);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMedicalRecord(@RequestParam String firstName,
                                                      @RequestParam String lastName) {
        boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Medical record deleted successfully");
    }
}