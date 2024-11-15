package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing MedicalRecord operations.
 * Handles business logic related to adding, updating, deleting, and retrieving medical records.
 */
@Service
public class MedicalRecordService {

    private static final Logger logger = LogManager.getLogger(MedicalRecordService.class);

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * Retrieves all medical records.
     *
     * @return List of all medical records.
     */
    public List<MedicalRecord> findAll() {
        logger.info("Retrieving all medical records.");
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        logger.debug("Retrieved {} medical records.", records.size());
        return records;
    }

    /**
     * Retrieves a medical record by its ID.
     *
     * @param id The UUID of the medical record.
     * @return The medical record if found, or null otherwise.
     */
    public MedicalRecord findById(UUID id) {
        logger.info("Searching for medical record with ID: {}", id);
        MedicalRecord record = medicalRecordRepository.findById(id);
        if (record != null) {
            logger.info("Medical record found: {}", record);
        } else {
            logger.error("No medical record found with ID: {}", id);
        }
        return record;
    }

    /**
     * Adds a new medical record.
     *
     * @param medicalRecord The medical record to add.
     * @throws IOException if an error occurs during data persistence.
     */
    public void addMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        logger.info("Adding medical record: {}", medicalRecord);
        medicalRecordRepository.addMedicalRecord(medicalRecord);
        logger.info("Medical record added successfully.");
    }

    /**
     * Updates an existing medical record.
     *
     * @param medicalRecord The medical record to update.
     * @return True if the medical record was updated, false otherwise.
     */
    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        logger.info("Updating medical record: {}", medicalRecord);
        Optional<MedicalRecord> existingRecordOpt = Optional.ofNullable(medicalRecordRepository.findById(medicalRecord.getId()));

        return existingRecordOpt.map(existingRecord -> {
            boolean isUpdated = updateFieldsIfNecessary(existingRecord, medicalRecord);
            if (isUpdated) {
                try {
                    medicalRecordRepository.updateMedicalRecord(existingRecord);
                    logger.info("Medical record updated successfully: {}", existingRecord);
                } catch (IOException e) {
                    logger.error("Error while updating medical record: {}", medicalRecord, e);
                    throw new RuntimeException(e);
                }
            } else {
                logger.debug("No changes detected for medical record: {}", medicalRecord);
            }
            return isUpdated;
        }).orElseGet(() -> {
            logger.error("Medical record not found for update: {}", medicalRecord.getId());
            return false;
        });
    }

    private boolean updateFieldsIfNecessary(MedicalRecord existingRecord, MedicalRecord newRecord) {
        boolean isUpdated = false;

        if (!existingRecord.getMedications().equals(newRecord.getMedications())) {
            logger.debug("Updating medications from {} to {}", existingRecord.getMedications(), newRecord.getMedications());
            existingRecord.setMedications(newRecord.getMedications());
            isUpdated = true;
        }

        if (!existingRecord.getAllergies().equals(newRecord.getAllergies())) {
            logger.debug("Updating allergies from {} to {}", existingRecord.getAllergies(), newRecord.getAllergies());
            existingRecord.setAllergies(newRecord.getAllergies());
            isUpdated = true;
        }

        if (!existingRecord.getBirthdate().equals(newRecord.getBirthdate())) {
            logger.debug("Updating birthdate from {} to {}", existingRecord.getBirthdate(), newRecord.getBirthdate());
            existingRecord.setBirthdate(newRecord.getBirthdate());
            isUpdated = true;
        }

        return isUpdated;
    }

    /**
     * Deletes a medical record by its ID.
     *
     * @param id The UUID of the medical record to delete.
     * @return True if the medical record was deleted, false otherwise.
     * @throws IOException if an error occurs during data persistence.
     */
    public boolean deleteMedicalRecord(UUID id) throws IOException {
        logger.info("Deleting medical record with ID: {}", id);
        boolean deleted = medicalRecordRepository.deleteMedicalRecord(id);
        if (deleted) {
            logger.info("Medical record deleted successfully: {}", id);
        } else {
            logger.error("Medical record not found for deletion: {}", id);
        }
        return deleted;
    }
}
