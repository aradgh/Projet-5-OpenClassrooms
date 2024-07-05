package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FireStationRepository extends Repository<FireStation> {

    FireStation findByAddress(String address);

    FireStation findByStation(String station);

    FireStation findByStationAndAddress(String station, String address);

    FireStation save(FireStation fireStation);

    List<FireStation> findAll();
}
