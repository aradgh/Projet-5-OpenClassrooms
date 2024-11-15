package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

/**
 * Controller for managing MedicalRecord-related endpoints.
 * Handles operations such as adding, updating, deleting, and retrieving medical records.
 */
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Adds a new medical record.
     *
     * @param medicalRecord The medical record to add.
     * @return A response indicating the outcome of the operation.
     * @throws IOException if an error occurs during data persistence.
     */
    @PostMapping
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws IOException {
        logger.info("Received request to add medical record: {}", medicalRecord);
        try {
            medicalRecordService.addMedicalRecord(medicalRecord);
            logger.info("Medical record added successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body("Medical record added successfully.");
        } catch (Exception e) {
            logger.error("Error while adding medical record: {}", medicalRecord, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the medical record.");
        }
    }

    /**
     * Updates an existing medical record.
     *
     * @param medicalRecord The medical record to update.
     * @return A response indicating the outcome of the operation.
     */
    @PutMapping
    public ResponseEntity<String> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("Received request to update medical record: {}", medicalRecord);
        try {
            boolean updated = medicalRecordService.updateMedicalRecord(medicalRecord);
            if (updated) {
                logger.info("Medical record updated successfully.");
                return ResponseEntity.ok("Medical record updated successfully.");
            } else {
                logger.error("Medical record not found for update: {}", medicalRecord.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical record not found.");
            }
        } catch (Exception e) {
            logger.error("Error while updating medical record: {}", medicalRecord, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the medical record.");
        }
    }

    /**
     * Deletes a medical record.
     *
     * @param medicalRecordId The ID of the medical record to delete.
     * @return A response indicating the outcome of the operation.
     * @throws IOException if an error occurs during data persistence.
     */
    @DeleteMapping
    public ResponseEntity<String> deleteMedicalRecord(@RequestParam UUID medicalRecordId) throws IOException {
        logger.info("Received request to delete medical record with ID: {}", medicalRecordId);
        try {
            boolean deleted = medicalRecordService.deleteMedicalRecord(medicalRecordId);
            if (deleted) {
                logger.info("Medical record deleted successfully.");
                return ResponseEntity.ok("Medical record deleted successfully.");
            } else {
                logger.error("Medical record not found for deletion: {}", medicalRecordId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medical record not found.");
            }
        } catch (Exception e) {
            logger.error("Error while deleting medical record with ID: {}", medicalRecordId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the medical record.");
        }
    }
}
