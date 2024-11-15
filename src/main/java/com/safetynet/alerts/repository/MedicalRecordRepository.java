package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.safetynet.alerts.repository.Data.medicalrecords;
import static com.safetynet.alerts.repository.JsonFileUtil.saveData;

/**
 * Repository for managing MedicalRecord data.
 * Handles CRUD operations and queries for medical records.
 */
@Repository
public class MedicalRecordRepository {

    private static final Logger logger = LogManager.getLogger(MedicalRecordRepository.class);

    /**
     * Retrieves all medical records.
     *
     * @return List of all medical records.
     */
    public List<MedicalRecord> findAll() {
        logger.info("Retrieving all medical records.");
        return medicalrecords;
    }

    /**
     * Retrieves a medical record by its ID.
     *
     * @param id The UUID of the medical record.
     * @return The medical record if found, or null otherwise.
     */
    public MedicalRecord findById(UUID id) {
        logger.info("Searching for medical record with ID: {}", id);
        MedicalRecord record = medicalrecords.stream()
            .filter(medicalRecord -> medicalRecord.getId().equals(id))
            .findFirst()
            .orElse(null);
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
        medicalrecords.add(medicalRecord);
        saveData();
        logger.info("Medical record added successfully.");
    }

    /**
     * Updates an existing medical record.
     *
     * @param medicalRecord The medical record to update.
     * @throws IOException if an error occurs during data persistence.
     */
    public void updateMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        logger.info("Updating medical record: {}", medicalRecord);
        Optional<MedicalRecord> existingRecordOpt = medicalrecords.stream()
            .filter(record -> record.getId().equals(medicalRecord.getId()))
            .findFirst();

        if (existingRecordOpt.isPresent()) {
            MedicalRecord existingRecord = existingRecordOpt.get();
            int index = medicalrecords.indexOf(existingRecord);
            medicalrecords.set(index, medicalRecord);
            saveData();
            logger.info("Medical record updated successfully: {}", medicalRecord);
        } else {
            logger.error("No medical record found for update with ID: {}", medicalRecord.getId());
        }
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
        Optional<MedicalRecord> recordToDelete = medicalrecords.stream()
            .filter(record -> record.getId().equals(id))
            .findFirst();

        if (recordToDelete.isPresent()) {
            medicalrecords.remove(recordToDelete.get());
            saveData();
            logger.info("Medical record deleted successfully: {}", id);
            return true;
        } else {
            logger.error("No medical record found for deletion with ID: {}", id);
            return false;
        }
    }

    /**
     * Finds a medical record by the given first and last name.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     * @return The medical record if found, or null otherwise.
     */
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        logger.info("Searching for medical record with firstName={} and lastName={}", firstName, lastName);

        MedicalRecord medicalRecord = medicalrecords.stream()
            .filter(record -> record.getFirstName().equals(firstName) && record.getLastName().equals(lastName))
            .findFirst()
            .orElse(null);

        if (medicalRecord != null) {
            logger.info("Medical record found: {}", medicalRecord);
        } else {
            logger.error("No medical record found for firstName={} and lastName={}", firstName, lastName);
        }

        return medicalRecord;
    }

}
