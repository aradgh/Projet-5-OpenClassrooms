package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/medicalrecord")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws IOException {
        medicalRecordService.addMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body("Medical record added successfully.");
    }

    @PutMapping
    public ResponseEntity<String> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws IOException {
        boolean updated = medicalRecordService.updateMedicalRecord(medicalRecord);
        if (updated) {
            return ResponseEntity.ok("Medical record updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical record not found.");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) throws IOException {
        boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (deleted) {
            return ResponseEntity.ok("Medical record deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical record not found.");
        }
    }
}
