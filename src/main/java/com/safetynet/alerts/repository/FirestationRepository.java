package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Firestation;
import org.springframework.stereotype.Repository;

import java.io.IOException;
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
}
