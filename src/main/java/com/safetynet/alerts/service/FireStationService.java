package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;

import java.util.List;

public class FireStationService {
    private FireStationRepository fireStationRepository;

    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    public FireStation findByAddress(String address) {
        return fireStationRepository.findByAddress(address);
    }

    public FireStation findByStation(String station) {
        return fireStationRepository.findByStation(station);
    }

    public FireStation findByStationAndAddress(String station, String address) {
        return fireStationRepository.findByStationAndAddress(station, address);
    }

    public FireStation save(FireStation fireStation) {
        return fireStationRepository.save(fireStation);
    }

    public List<FireStation> findAll() {
        return fireStationRepository.findAll();
    }

}
