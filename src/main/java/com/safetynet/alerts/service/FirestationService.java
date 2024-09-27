package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.FirestationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

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
        Firestation existingFirestation = firestationRepository.findById(firestation.getId());
        if (existingFirestation != null) {
            existingFirestation.setStation(firestation.getStation());
            existingFirestation.setAddress(firestation.getAddress());
            firestationRepository.updateFirestation(firestation);
            return true;
        }
        return false;
    }

    public boolean deleteFirestation(UUID firestationId) throws IOException {
        return firestationRepository.deleteFirestation(firestationId);
    }
}
