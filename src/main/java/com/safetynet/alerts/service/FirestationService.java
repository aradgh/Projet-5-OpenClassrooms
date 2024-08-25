package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.FirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirestationService {
    private final FirestationRepository firestationRepository;

    public FirestationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

    public Firestation addFirestation(Firestation firestation) {
        firestationRepository.save(firestation);
        return firestation;
    }

    public boolean updateFirestation(Firestation firestation) {
        Firestation existingFirestation = firestationRepository.findByAddress(firestation.getAddress());
        if (existingFirestation != null) {
//            Toute la logique métier se fait dans le service
            firestationRepository.save(firestation);
            return true;
        }
        return false;
    }

    public boolean deleteFirestation(String address) {
        Firestation existingFirestation = firestationRepository.findByAddress(address);
        if (existingFirestation != null) {
            firestationRepository.delete(existingFirestation);
            return true;
        }
        return false;
    }
}
