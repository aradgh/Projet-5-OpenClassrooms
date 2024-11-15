package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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

    /**
     * Updates the fields of an existing medical record if they differ from the new record.
     *
     * @param existingRecord The current medical record.
     * @param newRecord      The new medical record with updated data.
     * @return True if any field was updated, false otherwise.
     */
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

        if (isUpdated) {
            logger.info("Medical record updated: {}", existingRecord);
        } else {
            logger.debug("No fields were updated for medical record: {}", existingRecord);
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

    /**
     * Calculates the age of a person based on their birthdate.
     *
     * @param birthdate The birthdate in "MM/dd/yyyy" format.
     * @return The calculated age.
     */
    public int calculateAge(String birthdate) {
        logger.debug("Calculating age for birthdate: {}", birthdate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        int age = Period.between(birthDate, LocalDate.now()).getYears();

        logger.debug("Calculated age: {}", age);
        return age;
    }

    /**
     * Determines if a given person is a child (age <= 18).
     *
     * @param person The person to check.
     * @return True if the person is a child, false otherwise.
     */
    public boolean isChild(Person person) {
        logger.debug("Checking if person is a child: {}", person);

        MedicalRecord personRecord = getMedicalRecordByPerson(person.getFirstName(), person.getLastName());
        if (personRecord == null) {
            logger.error("No medical record found for person: {}", person);
            return false;
        }

        boolean isChild = calculateAge(personRecord.getBirthdate()) <= 18;
        logger.debug("Person {} is a child: {}", person.getFirstName(), isChild);
        return isChild;
    }

    /**
     * Retrieves a medical record for a given person based on their first and last name.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     * @return The medical record if found, or null otherwise.
     */
    public MedicalRecord getMedicalRecordByPerson(String firstName, String lastName) {
        logger.info("Retrieving medical record for person: {} {}", firstName, lastName);

        MedicalRecord record = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
        if (record != null) {
            logger.info("Medical record found: {}", record);
        } else {
            logger.error("No medical record found for person: {} {}", firstName, lastName);
        }
        return record;
    }

}
