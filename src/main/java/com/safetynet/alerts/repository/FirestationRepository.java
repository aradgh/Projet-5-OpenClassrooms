package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Firestation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.safetynet.alerts.model.Data.firestations;
import static com.safetynet.alerts.repository.JsonFileUtil.saveData;

/**
 * Repository for managing Firestation data.
 * Handles CRUD operations and data queries for firestations.
 */
@Repository
public class FirestationRepository {

    private static final Logger logger = LogManager.getLogger(FirestationRepository.class);

    /**
     * Retrieves all firestations.
     *
     * @return List of all firestations.
     */
    public List<Firestation> findAll() {
        logger.info("Retrieving all firestations.");
        return firestations;
    }

    /**
     * Retrieves a firestation by its ID.
     *
     * @param firestationId The UUID of the firestation.
     * @return The firestation if found, null otherwise.
     */
    public Firestation findById(UUID firestationId) {
        logger.info("Searching for firestation with ID: {}", firestationId);
        Firestation firestation = firestations.stream()
            .filter(f -> f.getId().equals(firestationId))
            .findFirst()
            .orElse(null);
        if (firestation != null) {
            logger.info("Firestation found: {}", firestation);
        } else {
            logger.error("No firestation found with ID: {}", firestationId);
        }
        return firestation;
    }

    /**
     * Adds a new firestation.
     *
     * @param firestation The firestation to add.
     * @throws IOException if an error occurs while saving data.
     */
    public void addFirestation(Firestation firestation) throws IOException {
        logger.info("Adding firestation: {}", firestation);
        firestations.add(firestation);
        saveData();
        logger.info("Firestation added successfully.");
    }

    /**
     * Updates an existing firestation.
     *
     * @param firestation The firestation to update.
     * @throws IOException if an error occurs while saving data.
     */
    public void updateFirestation(Firestation firestation) throws IOException {
        logger.info("Updating firestation: {}", firestation);
        Firestation existingFirestation = this.findById(firestation.getId());
        if (existingFirestation != null) {
            firestations.set(firestations.indexOf(existingFirestation), firestation);
            saveData();
            logger.info("Firestation updated successfully: {}", firestation);
        } else {
            logger.error("No firestation found for update with ID: {}", firestation.getId());
        }
    }

    /**
     * Deletes a firestation by its ID.
     *
     * @param firestationId The UUID of the firestation.
     * @return True if the firestation was deleted, false otherwise.
     * @throws IOException if an error occurs while saving data.
     */
    public boolean deleteFirestation(UUID firestationId) throws IOException {
        logger.info("Deleting firestation with ID: {}", firestationId);
        Firestation firestationToDelete = this.findById(firestationId);
        if (firestationToDelete != null) {
            firestations.remove(firestationToDelete);
            saveData();
            logger.info("Firestation deleted successfully: {}", firestationId);
            return true;
        }
        logger.error("No firestation found for deletion with ID: {}", firestationId);
        return false;
    }

    /**
     * Retrieves firestations by station number.
     *
     * @param stationNumber The station number.
     * @return List of firestations matching the station number.
     */
    public List<Firestation> findByStation(int stationNumber) {
        logger.info("Searching for firestations with station number: {}", stationNumber);
        List<Firestation> result = firestations.stream()
            .filter(f -> f.getStation() == stationNumber)
            .toList();
        logger.info("Found {} firestations with station number: {}", result.size(), stationNumber);
        return result;
    }

    /**
     * Retrieves a firestation by its address.
     *
     * @param address The address of the firestation.
     * @return An Optional containing the firestation if found, or empty otherwise.
     */
    public Optional<Firestation> findByAddress(String address) {
        logger.info("Searching for firestation with address: {}", address);
        Optional<Firestation> firestation = firestations.stream()
            .filter(f -> f.getAddress().equals(address))
            .findFirst();
        if (firestation.isPresent()) {
            logger.info("Firestation found with address: {}", address);
        } else {
            logger.error("No firestation found with address: {}", address);
        }
        return firestation;
    }

    /**
     * Retrieves firestations matching a set of station numbers.
     *
     * @param stationNumbers The set of station numbers.
     * @return List of firestations matching the station numbers.
     */
    public List<Firestation> findByStations(Set<Integer> stationNumbers) {
        logger.info("Searching for firestations with station numbers: {}", stationNumbers);
        List<Firestation> result = firestations.stream()
            .filter(f -> stationNumbers.contains(f.getStation()))
            .toList();
        logger.info("Found {} firestations matching station numbers: {}", result.size(), stationNumbers);
        return result;
    }
}
