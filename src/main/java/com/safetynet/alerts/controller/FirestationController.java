package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @PostMapping
    public ResponseEntity<String> addFirestation(@RequestBody Firestation firestation) throws IOException {
        firestationService.addFirestation(firestation);
        return ResponseEntity.status(HttpStatus.CREATED).body("Firestation added successfully.");
    }

    @PutMapping
    public ResponseEntity<String> updateFirestation(@RequestBody Firestation firestation) throws IOException {
        boolean updated = firestationService.updateFirestation(firestation);
        if (updated) {
            return ResponseEntity.ok("Firestation updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation not found.");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFirestation(@RequestParam String address) throws IOException {
        boolean deleted = firestationService.deleteFirestation(address);
        if (deleted) {
            return ResponseEntity.ok("Firestation deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation not found.");
        }
    }
}
