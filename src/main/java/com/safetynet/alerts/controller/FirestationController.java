package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireAlertDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.FirestationCoverageDTO;
import com.safetynet.alerts.service.FirestationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
public class FirestationController {
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @PostMapping("/firestation")
    public ResponseEntity<String> addFirestation(@RequestBody Firestation firestation) throws IOException {
        firestationService.addFirestation(firestation);
        return ResponseEntity.status(HttpStatus.CREATED).body("Firestation added successfully.");
    }

    @PutMapping("/firestation")
    public ResponseEntity<String> updateFirestation(@RequestBody Firestation firestation) {
        boolean updated = firestationService.updateFirestation(firestation);
        if (updated) {
            return ResponseEntity.ok("Firestation updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation not found.");
        }
    }

    @DeleteMapping("/firestation")
    public ResponseEntity<String> deleteFirestation(@RequestParam UUID firestationId) throws IOException {
        boolean deleted = firestationService.deleteFirestation(firestationId);
        if (deleted) {
            return ResponseEntity.ok("Firestation deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation not found.");
        }
    }

    @GetMapping("/firestation")
    public ResponseEntity<String> getPersonsByStation(@RequestParam int stationNumber) {
        FirestationCoverageDTO firestationCoverageDTO = firestationService.getCoverageByStation(stationNumber);
        return ResponseEntity.ok(firestationCoverageDTO.toString());
    }

    @GetMapping("/fire")
    public ResponseEntity<String> getFireAlertByAddress(@RequestParam String address) {
        FireAlertDTO fireAlertDTO = firestationService.getResidentsByAddress(address);

        if (fireAlertDTO.getResidents().isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No residents found at the specified address.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fireAlertDTO.toString());
    }
}
