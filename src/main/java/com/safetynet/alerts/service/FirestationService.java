package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.FirestationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FirestationService {
    private final FirestationRepository firestationRepository;

    public FirestationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    public void addFirestation(Firestation firestation) throws IOException {
        firestationRepository.addFirestation(firestation);
    }

    public boolean updateFirestation(Firestation firestation) throws IOException {
        Firestation existingFirestation = firestationRepository.findByAddress(firestation.getAddress());
        if (existingFirestation != null) {
            existingFirestation.setStation(firestation.getStation());
            existingFirestation.setAddress(firestation.getAddress());
            firestationRepository.updateFirestation(firestation);
            return true;
        }
        return false;
    }

    public boolean deleteFirestation(String address) throws IOException {
        Firestation existingFirestation = firestationRepository.findByAddress(address);
        if (existingFirestation != null) {
            firestationRepository.deleteFirestation(existingFirestation.getAddress());
            return true;
        }
        return false;
    }
}
