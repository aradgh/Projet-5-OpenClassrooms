package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Firestation;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FirestationRepository {
    private Map<String, Firestation> firestations = new HashMap<>();

    public FirestationRepository(List<Firestation> firestations) {
        for (Firestation firestation : firestations) {
            this.firestations.put(firestation.getAddress(), firestation);
        }
    }

    public List<Firestation> findAll() {
        return new ArrayList<>(firestations.values());
    }

    public Firestation findByAddress(String address) {
        return firestations.get(address);
    }

    public void addFirestation(Firestation firestation) throws IOException {
        firestations.put(firestation.getAddress(), firestation);
        saveToFile();
    }

    public void updateFirestation(Firestation firestation) throws IOException {
        if (firestations.containsKey(firestation.getAddress())) {
            firestations.put(firestation.getAddress(), firestation);
            saveToFile();
        }
    }

    public void deleteFirestation(String address) throws IOException {
        if (firestations.containsKey(address)) {
            firestations.remove(address);
            saveToFile();
        }
    }

    private void saveToFile() throws IOException {
        JsonFileUtil.saveData();
    }
}
