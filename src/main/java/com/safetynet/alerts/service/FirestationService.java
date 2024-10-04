package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

    public boolean updateFirestation(Firestation firestation) {
        Optional<Firestation> existingFirestationOpt = Optional.ofNullable(
            firestationRepository.findById(firestation.getId()));
        return existingFirestationOpt
            .map(existingFirestation -> {
                boolean isUpdated = updateStationIfNecessary(existingFirestation, firestation.getStation());
                isUpdated |= updateAddressIfNecessary(existingFirestation, firestation.getAddress());

                if (isUpdated) {
                    try {
                        firestationRepository.updateFirestation(existingFirestation);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                return isUpdated;
            })
            .orElse(false);
    }

    public boolean deleteFirestation(UUID firestationId) throws IOException {
        return firestationRepository.deleteFirestation(firestationId);
    }

//    public String getPersonsListByStation(int stationNumber) {
//        List<Firestation> firestationsByStation = firestationRepository.findAll()
//            .stream()
//            .filter(firestation -> firestation.getStation() == stationNumber)
//            .toList();
//        List<Person> personsListByStation = List.of();
//        for (Firestation firestation : firestationsByStation) {
//            personsListByStation.add(getPersonsListByAddress(firestation.getAddress()));
//        }
//        return firestationRepository.getPersonsListByStation(stationNumber);
//    }


    private boolean updateStationIfNecessary(Firestation existingFirestation, int newStation) {
        if (newStation > 0 && existingFirestation.getStation() != newStation) {
            existingFirestation.setStation(newStation);
            return true;
        }
        return false;
    }

    private boolean updateAddressIfNecessary(Firestation existingFirestation, String newAddress) {
        if (newAddress != null && !newAddress.isEmpty() && !existingFirestation.getAddress().equals(newAddress)) {
            existingFirestation.setAddress(newAddress);
            return true;
        }
        return false;
    }
}
