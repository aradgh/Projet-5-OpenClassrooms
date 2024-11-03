package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Firestation;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.safetynet.alerts.repository.Data.firestations;
import static com.safetynet.alerts.repository.JsonFileUtil.saveData;

@Repository
public class FirestationRepository {
    public List<Firestation> findAll() {
        return firestations;
    }

    public Firestation findById(UUID firestationId) {
        return firestations.stream()
            .filter(firestation -> firestation.getId().equals(firestationId))
            .findFirst()
            .orElse(null);
    }

    public void addFirestation(Firestation firestation) throws IOException {
        firestations.add(firestation);
        saveData();
    }

    public void updateFirestation(Firestation firestation) throws IOException {
        if (this.findById(firestation.getId()) != null) {
            firestations.set(firestations.indexOf(this.findById(firestation.getId())), firestation);
            saveData();
        }
    }

    public boolean deleteFirestation(UUID firestationId) throws IOException {
        if (this.findById(firestationId) != null) {
            firestations.remove(this.findById(firestationId));
            saveData();
            return true;
        }
        return false;
    }

    public List<Firestation> findByStation(int stationNumber) {
        return firestations.stream()
            .filter(firestation -> firestation.getStation() == stationNumber)
            .toList();
    }

    public Optional<Firestation> findByAddress(String address) {
        return firestations.stream()
            .filter(firestation -> firestation.getAddress().equals(address))
            .findFirst();
    }

    public List<Firestation> findByStations(Set<Integer> stationNumbers) {
        return firestations.stream()
            .filter(firestation -> stationNumbers.contains(firestation.getStation()))
            .toList();
    }
}
