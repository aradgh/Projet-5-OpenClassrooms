package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireAlertDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.FirestationCoverageDTO;
import com.safetynet.alerts.model.FloodStationDTO;
import com.safetynet.alerts.service.FirestationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller to manage Firestation-related endpoints.
 * Handles operations such as adding, updating, deleting, and retrieving firestation data.
 */
@RestController
public class FirestationController {

    private static final Logger logger = LogManager.getLogger(FirestationController.class);

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Adds a new firestation.
     *
     * @param firestation The firestation to add.
     * @return A response indicating the outcome of the operation.
     * @throws IOException if an error occurs during data persistence.
     */
    @PostMapping("/firestation")
    public ResponseEntity<String> addFirestation(@RequestBody Firestation firestation) throws IOException {
        logger.info("Received request to add a new firestation: {}", firestation);
        firestationService.addFirestation(firestation);
        logger.info("Firestation added successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body("Firestation added successfully.");
    }

    /**
     * Updates an existing firestation.
     *
     * @param firestation The firestation to update.
     * @return A response indicating the outcome of the operation.
     */
    @PutMapping("/firestation")
    public ResponseEntity<String> updateFirestation(@RequestBody Firestation firestation) {
        logger.info("Received request to update firestation: {}", firestation);
        boolean updated = firestationService.updateFirestation(firestation);

        if (updated) {
            logger.info("Firestation updated successfully.");
            return ResponseEntity.ok("Firestation updated successfully.");
        } else {
            logger.error("Firestation not found for update: {}", firestation.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation not found.");
        }
    }

    /**
     * Deletes a firestation.
     *
     * @param firestationId The ID of the firestation to delete.
     * @return A response indicating the outcome of the operation.
     * @throws IOException if an error occurs during data persistence.
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<String> deleteFirestation(@RequestParam UUID firestationId) throws IOException {
        logger.info("Received request to delete firestation with ID: {}", firestationId);
        boolean deleted = firestationService.deleteFirestation(firestationId);

        if (deleted) {
            logger.info("Firestation deleted successfully.");
            return ResponseEntity.ok("Firestation deleted successfully.");
        } else {
            logger.error("Firestation not found for deletion: {}", firestationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation not found.");
        }
    }

    /**
     * Retrieves information about persons covered by a firestation.
     *
     * @param stationNumber The firestation number.
     * @return A response with the firestation coverage data.
     */
    @GetMapping("/firestation")
    public ResponseEntity<String> getPersonsByStation(@RequestParam int stationNumber) {
        logger.info("Received request to retrieve persons covered by firestation number: {}", stationNumber);
        FirestationCoverageDTO firestationCoverageDTO = firestationService.getCoverageByStation(stationNumber);
        logger.info("Successfully retrieved firestation coverage: {}", firestationCoverageDTO);
        return ResponseEntity.ok(firestationCoverageDTO.toString());
    }

    /**
     * Retrieves information about residents living at a specific address.
     *
     * @param address The address to query.
     * @return A response with the fire alert data.
     */
    @GetMapping("/fire")
    public ResponseEntity<String> getFireAlertByAddress(@RequestParam String address) {
        logger.info("Received request to retrieve fire alert for address: {}", address);
        FireAlertDTO fireAlertDTO = firestationService.getResidentsByAddress(address);

        if (fireAlertDTO.getResidents().isEmpty()) {
            logger.info("No residents found at address: {}", address);
            return ResponseEntity.status(HttpStatus.OK).body("No residents found at the specified address.");
        }

        logger.info("Successfully retrieved fire alert for address: {}", address);
        return ResponseEntity.status(HttpStatus.OK).body(fireAlertDTO.toString());
    }

    /**
     * Retrieves information about households covered by one or more firestations.
     *
     * @param stations The firestation numbers.
     * @return A response with the flood station data.
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<String> getFloodStations(@RequestParam Set<Integer> stations) {
        logger.info("Received request to retrieve flood station data for stations: {}", stations);
        List<FloodStationDTO> households = firestationService.getHouseholdsByStations(stations);

        if (households.isEmpty()) {
            logger.info("No households found for firestations: {}", stations);
            return ResponseEntity.status(HttpStatus.OK).body("No households found for the specified stations.");
        }

        String result = households.stream()
            .map(FloodStationDTO::toString)
            .collect(Collectors.joining("\n"));

        logger.info("Successfully retrieved flood station data for stations: {}", stations);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
