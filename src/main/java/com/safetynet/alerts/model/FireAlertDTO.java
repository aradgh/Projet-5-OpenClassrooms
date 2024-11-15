package com.safetynet.alerts.model;

import java.util.Set;
import java.util.stream.Collectors;

public class FireAlertDTO {

    private final int firestationNumber;
    private final Set<ResidentInfoDTO> residents;

    public FireAlertDTO(int firestationNumber, Set<ResidentInfoDTO> residents) {
        this.firestationNumber = firestationNumber;
        this.residents = residents;
    }

    public Set<ResidentInfoDTO> getResidents() {
        return residents;
    }

    @Override
    public String toString() {
        String residentsString = residents.stream()
            .map(ResidentInfoDTO::toString)
            .collect(Collectors.joining(", "));

        return "FireAlertDTO { " +
               "firestationNumber: " + firestationNumber +
               ", residents: [" + residentsString + "\n]" +
               " }";
    }
}
